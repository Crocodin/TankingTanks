# Liquibase Migration

Why would you use Liquibase? Well let's say we work in a project and someone changes the tabes adding a column or maybe a full on table. What would you do?

Well using Liquibase that person writes a change file (`xml` or `yaml`) that has the changes down. This is an example of such a file that you will find in this repository.

```xml
<changeSet id="007-add-column-delete-tank" author="Crocodin">
    <addColumn tableName="tank">
        <column name="is_deleted" type="boolean" defaultValue="false">
            <constraints nullable="false"/>
        </column>
        <column name="deleted_at" type="timestamp"/>
        <column name="deleted_by" type="varchar(100)"/>
    </addColumn>
</changeSet>
```

For this process i want in the db and made an initial database commit. This is the `001-initial-schema.xml` so that anyone in production can have the db by just running the application.

A problem I ran into is that the initial commit can't be in master.xml if the columns already exist in the db because Liquibase check the db logs that it makes to check if it up to date and if the tables already exists the app crashes.

To fix this problem in the real word we would just exclude the one file from masters how ever for this to be a learning experience i **drop the database**. :)

Another problem i ran into is that Hibernate will update the db himself if a column is not up to date. The simple solution was just to change in `application.properties` the `spring.jpa.hibernate.ddl-auto=` from `update` to `validate`.

# Optimistic Locking

Why would you use Optimistic Locking? Well let's say two users load the same tank record at the same time. User A changes the name and saves. Then User B (who loaded the old data) changes the year and saves — silently overwriting User A's change. This is called a **lost update**.

Optimistic Locking fixes this by adding a `version` column to the table. Every time a record is updated, the version increments. If two users load the same record and one saves first, the other's save will be rejected because their version is now outdated.

This is the Liquibase migration that adds the version column:

```xml
<changeSet id="007-add-column-version-tank" author="Crocodin">
    <addColumn tableName="tank">
        <column name="version" type="int" defaultValueNumeric="0">
            <constraints nullable="false"/>
        </column>
    </addColumn>
</changeSet>
```

On the Java side, all you need is the `@Version` annotation on the field in your entity. Hibernate handles everything else automatically:

```java
@Version
private Integer version;
```

When User B tries to save, Hibernate generates this under the hood:

```sql
UPDATE tank SET tank_name = 'Updated by User B', version = 1 
    WHERE tank_id = 1 AND version = 0
```

Since User A already updated the record, `version` is now `1` in the database. The `WHERE version = 0` matches nothing, Hibernate detects the conflict and throws `ObjectOptimisticLockingFailureException`.

The reason I used `TransactionTemplate` instead of `@Transactional` on the test is that optimistic locking exceptions are thrown at **commit time**. If the whole test runs in one transaction there is nothing to commit and the conflict never triggers. `TransactionTemplate` lets you manually control transaction boundaries so each users save is commited independently.

A problem I ran into is that `@SoftDelete` and lazy loading don't work together.

The simple fix was to change the fetch type from `LAZY` to `EAGER` on the association pointing to the soft deleted entity. :)

# Soft delete

We all konw why you would use solf deleting. So how i implemented this. Well using SpringBoot was esey, i just added the `@SolfDelete` on the OMR entity. Then in the JpaRepository I added two main features to work alongside the soft delete.

### Hard delete

Hard delete just bypasses the ORM and just deletes the column with native SQL.

```java
@Modifying
@Query(value = "DELETE FROM tank WHERE tank_id = :id", nativeQuery = true)
void hardDeleteById(@Param("id") Integer id);
```

### Restore deleted

What if we want to undelete something well just change the delete column form `is_delete` from 1 to 0 

```java
@Modifying
@Query(value = "UPDATE tank SET is_deleted = 0, deleted_at = NULL, deleted_by = NULL WHERE tank_id = :id", nativeQuery = true)
void restoreById(@Param("id") Integer id);
```
package ubb.dbsm.domain.validator;

import ubb.dbsm.domain.Tank;

import java.time.LocalDate;

public class TankValidator implements ValidatorStrategy<Tank> {
    @Override
    public boolean validate(Tank tank) {
        int year = tank.getYearOfProduction();
        return (1900 < year && year < LocalDate.now().getYear());
    }
}

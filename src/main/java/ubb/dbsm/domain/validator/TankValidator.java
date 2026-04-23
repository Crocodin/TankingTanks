package ubb.dbsm.domain.validator;

import org.springframework.stereotype.Component;
import ubb.dbsm.domain.Tank;

import java.time.LocalDate;

@Component
public class TankValidator implements ValidatorStrategy<Tank> {
    @Override
    public boolean validate(Tank tank) {
        int year = tank.getYearOfProduction();
        return (1900 < year && year < LocalDate.now().getYear() + 1);
    }
}

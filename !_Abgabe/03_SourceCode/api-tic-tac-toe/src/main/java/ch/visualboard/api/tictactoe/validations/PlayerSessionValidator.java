package ch.visualboard.api.tictactoe.validations;

import java.util.Collections;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PlayerSessionValidator implements ConstraintValidator<ValidPlayerSession, HttpSession>
{
    @Override
    public boolean isValid(final HttpSession session, final ConstraintValidatorContext constraintValidatorContext)
    {
        try {
            return Collections.list(session.getAttributeNames()).stream()
                .anyMatch(objects -> "player".equalsIgnoreCase(objects));
        }
        catch (Exception e) {
            return false;
        }
    }
}

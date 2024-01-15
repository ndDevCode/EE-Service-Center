package controller.util;

import dto.StaffDto;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
    private ValidationUtil(){}

    public static boolean validate(String validateString,ValidationType type){
        Pattern pattern = Pattern.compile(type.toString());
        Matcher matcher = pattern.matcher(validateString);
        return matcher.matches();
    }

    public static boolean validateForSave(StaffDto staffDto){
        return validate(staffDto.getFirstName(),ValidationType.TEXT_ONLY) &&
                validate(staffDto.getLastName(),ValidationType.TEXT_ONLY) &&
                validate(staffDto.getContactNo(),ValidationType.CONTACT_NO) &&
                validate(staffDto.getEmail(),ValidationType.EMAIL) &&
                validate(staffDto.getPassword(),ValidationType.PASSWORD) &&
                !Objects.equals(staffDto.getRole(), "") &&
                staffDto.getRole()!=null;
    }

    public static boolean validateForUpdate(StaffDto staffDto){
        boolean isPasswordValidated = validate(staffDto.getPassword(), ValidationType.PASSWORD);
        boolean isStaffIdAvailable = false;

        if(staffDto.getStaffId()!=null){
            isStaffIdAvailable = true;
        }

        if(staffDto.getPassword().isEmpty()){
            isPasswordValidated = true;
        }

        return validate(staffDto.getFirstName(),ValidationType.TEXT_ONLY) &&
                validate(staffDto.getLastName(),ValidationType.TEXT_ONLY) &&
                validate(staffDto.getContactNo(),ValidationType.CONTACT_NO) &&
                validate(staffDto.getEmail(),ValidationType.EMAIL) &&
                isPasswordValidated &&
                isStaffIdAvailable &&
                !Objects.equals(staffDto.getRole(), "") &&
                staffDto.getRole()!=null;
    }
}

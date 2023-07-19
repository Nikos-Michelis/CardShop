package gr.uth.cardshop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationForm {

    ValidationForm(){
    }
    boolean isValidFullName(final String name) {
        Pattern pattern;
        Matcher matcher;
        final String checkName = "^(?=.{1,20}$)[A-Za-z]+(?:\\s[A-Za-z]+)*$";

        pattern = Pattern.compile(checkName);
        matcher = pattern.matcher(name);
        return matcher.matches();
    }
    boolean isValidEmail(final String email) {
        Pattern pattern;
        Matcher matcher;
        final String checkEmail ="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";


        pattern = Pattern.compile(checkEmail);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
    boolean isValidPhone(final String phone) {
        Pattern pattern;
        Matcher matcher;
        final String checkPhone = "^\\d{10}$";

        pattern = Pattern.compile(checkPhone);
        matcher = pattern.matcher(phone);
        return matcher.matches();
    }
    boolean isValidAddress(final String address) {
        Pattern pattern;
        Matcher matcher;
        final String checkName = "^[a-zA-Z0-9 ]{1,40}$";

        pattern = Pattern.compile(checkName);
        matcher = pattern.matcher(address);
        return matcher.matches();
    }
    boolean isValidCity(final String city) {
        Pattern pattern;
        Matcher matcher;
        final String checkName = "^[a-zA-Z]{1,40}$";

        pattern = Pattern.compile(checkName);
        matcher = pattern.matcher(city);
        return matcher.matches();
    }
    boolean isValidPostalCode(final String code) {
        Pattern pattern;
        Matcher matcher;
        final String checkPhone = "^\\d{5}$";

        pattern = Pattern.compile(checkPhone);
        matcher = pattern.matcher(code);
        return matcher.matches();
    }
    boolean isValidCountry(final String country) {
        Pattern pattern;
        Matcher matcher;
        final String checkName = "^[a-zA-Z]{1,40}$";

        pattern = Pattern.compile(checkName);
        matcher = pattern.matcher(country);
        return matcher.matches();
    }

}
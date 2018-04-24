package app;

public class ServiseUtil {

    //Обрезает ноль после точки дробного числа
    public static String cutZero(Double number) {
        int rightPart = number.intValue();
        if ((number - rightPart) == 0)
            return String.valueOf(rightPart);
        else
            return String.valueOf(number);
    }

}

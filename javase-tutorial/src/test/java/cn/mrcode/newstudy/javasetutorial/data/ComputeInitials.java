package cn.mrcode.newstudy.javasetutorial.data;

/**
 * <pre>
 *  Version         Date            Author          Description
 * ---------------------------------------------------------------------------------------
 *  1.0.0           2017/12/14     zhuqiang        -
 * </pre>
 * @author zhuqiang
 * @version 1.0.0 2017/12/14 17:09
 * @date 2017/12/14 17:09
 * @since 1.0.0
 */
public class ComputeInitials {
    public static void main(String[] args) {
        String myName = "Fred F. Flintstone";
        StringBuffer myInitials = new StringBuffer();
        int length = myName.length();

        for (int i = 0; i < length; i++) {
            if (Character.isUpperCase(myName.charAt(i))) {
                myInitials.append(myName.charAt(i));
            }
        }
        System.out.println("My initials are: " + myInitials);
    }
}

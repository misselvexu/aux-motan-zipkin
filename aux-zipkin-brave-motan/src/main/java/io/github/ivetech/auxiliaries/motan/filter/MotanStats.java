package io.github.ivetech.auxiliaries.motan.filter;

/**
 * io.github.ivetech.auxiliaries.motan.filter
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 29/12/2016.
 */
public enum MotanStats {

    /**
     * 成功
     */
    SUCCESS,

    /**
     * 失败
     */
    FAIL,

    /**
     * 异常
     */
    EXCEPTION;

    public static MotanStats parse (Object s) {
        try {
            if (s == null) {
                return FAIL;
            }
            return MotanStats.valueOf(s.toString());
        } catch (Exception e) {
        }
        return FAIL;
    }
    
}

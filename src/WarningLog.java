public class WarningLog {
    private String code;
    private String lvl;
    private String method;
    private String exClass;
    private String desc;

    WarningLog(String code,  String lvl, String method, String exClass, String desc){
        this.code = code;
        this.lvl = lvl;
        this.method = method;
        this.exClass = exClass;
        this.desc = desc;
    }
}

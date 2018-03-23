public class ErrorLog {
    private String id;
    private String some;
    private String lvl;
    private String method;
    private String error;
    private String classError;
    ErrorLog(String id, String some, String lvl, String method, String error,String classError){
        this.id = id;
        this.some = some;
        this.lvl = lvl;
        this.error = error;
        this.method = method;
        this.classError = classError;
    }
}
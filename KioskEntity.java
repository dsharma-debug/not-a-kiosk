import java.io.Serializable;
public abstract class KioskEntity implements Serializable {
    private String code;
    private String name;
    public KioskEntity(String code, String name) {
        this.code = code;
        this.name = name;
    }
    protected String getCode() {
        return code;
    }
    protected String getNameInternal() {
        return name;
    }
    protected void setNameInternal(String name) {
        this.name = name;
    }
}

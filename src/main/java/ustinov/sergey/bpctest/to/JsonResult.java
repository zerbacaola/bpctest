package ustinov.sergey.bpctest.to;

public class JsonResult<T> {

    public enum Result {
        OK,
        ERROR
    }

    private long time;
    private Result result;
    private String errorMessage;
    private T data;

    public JsonResult(T data) {
        time = System.currentTimeMillis();
        this.result = Result.OK;
        this.data = data;
    }

    public JsonResult(Result result, T data, String errorMessage) {
        time = System.currentTimeMillis();
        this.result = result;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
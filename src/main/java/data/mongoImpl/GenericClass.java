package data.mongoImpl;

/**
 * Created by Christian on 15-05-2017.
 */
public class GenericClass<T> {
    private final Class<T> type;

    public GenericClass(Class<T> type) {
        this.type=type;
    }
    public Class<T> getMyType(){
        return this.type;
    }

}

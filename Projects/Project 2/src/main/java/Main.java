import se.kth.id1020.Driver;
import se.kth.id1020.TinySearchEngineBase;

/**
 * Created by Albin on 2016-10-07.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        TinySearchEngineBase searchEngine = new TinySearchEngine();
        Driver.run(searchEngine);
    }
}

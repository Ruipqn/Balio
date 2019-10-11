package GameFunctions;

public class Level
{
    // static variable single_instance of type Singleton
    private static Level single_instance = null;

    // variable of type String
    private int l;

    // private constructor restricted to this class itself
    private Level()
    {
        l = 1;
    }

    // static method to create instance of Singleton class
    public static Level getInstance()
    {
        if (single_instance == null)
            single_instance = new Level();

        return single_instance;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }
}

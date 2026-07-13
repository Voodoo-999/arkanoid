/**
 * Counter.
 */
public class Counter {
    private int count;

    /**
     * the constructor.
     */
    public Counter() {
        this.count = 0;
    }

    // add number to current count.
    /**
     * @param number to increase by
     */
    void increase(int number) {
        this.count += number;
    }

    // subtract number from current count.
    /**
     * @param number to dcrease by
     */
    void decrease(int number) {
        this.count -= number;
    }

    // get current count.
    /**
     * @return current count
     */
    int getValue() {
        return this.count;
    }
}

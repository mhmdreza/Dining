package fathi.shakhes.helpers;

public interface DialogListener {
    void onPositiveButtonClick();

    default void onNegativeButtonClick() {
    }
}

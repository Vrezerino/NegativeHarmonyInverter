package com.vrezerino.negativeharmonyinverter;
import javax.swing.SwingUtilities;

public class NegativeHarmony {
    
    public static void initUI() {
        NegativeHarmonyUI negativeHarmonyUI = new NegativeHarmonyUI();
        negativeHarmonyUI.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            initUI();
        });
    }
}

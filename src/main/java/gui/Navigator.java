package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class Navigator {

    /**
     * Replaces the frame's content with {@code panel};
     * revalidate re-runs layout, repaint redraws.
     */
    public static void show(JFrame frame, JPanel panel) {
        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private Navigator() {
    }
}

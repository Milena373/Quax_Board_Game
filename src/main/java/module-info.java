module comp20050.qssboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    requires org.testfx.junit5;
    requires org.testfx;

    opens comp20050.qssboard to javafx.fxml, org.testfx, org.testfx.junit5;
    exports comp20050.qssboard;
}
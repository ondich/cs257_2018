/**
 * Main.java
 * Jeff Ondich, 19 Nov 2014
 *
 * The main program for a tiny demo pong-like program in JavaFX. The goal of
 * this program is to illustrate two techniques interacting: (1) Timer-based
 * animation, and (2) keystroke handling. As a sidelight, this demo also
 * introduces AnchorPane to keep the gameboard tied to the window's boundaries,
 * and to keep the paddle tied to the bottom of the gameboard.
 */

package pong;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        FXMLLoader loader = new FXMLLoader(getClass().getResource("pong.fxml"));
        Parent root = (Parent)loader.load();
        Controller controller = loader.getController();

        // Set up a KeyEvent handler so we can respond to keyboard activity.
//        root.setOnKeyPressed(controller);

        // One of the most persistent problems I have had in learning to use JavaFX has been
        // what felt like erratic behavior of the KeyEvent handling system. For example, in
        // this application, go to pong.fxml and delete or comment out the <Button> object.
        // When you run the resulting program, you may note that the handler(KeyEvent) method
        // in Controller never gets called. So you delete a button and the keyboard control
        // stops working, even though the button seems to have nothing to do with the keyboard.
        // Weird and maddening. Put the button back in, and now KeyEvents show up. ???
        //
        // What's going on? Let's start here: https://docs.oracle.com/javafx/2/events/processing.htm
        // This whole page is worth reading, but let's go down to the Target Selection section,
        // where it says: "For key events, the target is the node that has focus." If you then
        // read through the Route Construction and Event Bubbling Phase sections, you'll start
        // to get a hint of what's happening.
        //
        // Consider our pong application with the Button intact. When the window opens, you can
        // see a blue outline around the Pause button. That's familiar to experienced users of modern
        // software--it means that the Pause button is the item with "focus." Sometimes, this means
        // that if you hit Enter, the button will act as though it's clicked. Sometimes, you can expect
        // hitting the Tab key to jump you from item to item in the user interface. But in any case,
        // the blue outline is telling us about focus.
        //
        // If you look at the Event Dispatch Chain diagram in the page linked above, you can see how
        // our chain looks: Stage -> Scene -> AnchorPane -> Button, plus more children of AnchorPane.
        // Because our Button has the focus, it will be chosen as the KeyEvent target if the user types D, say.
        // Because of event bubbling, the KeyEvent will eventually end up at our AnchorPane, which has
        // a KeyEvent handler (namely, our Controller object). So the Controller handles the KeyEvent,
        // and the D gets turned into rightward motion of the pong paddle. Cool.
        //
        // But what happens if we delete the Button from pong.fxml? It turns out that none of the remaining
        // nodes in pong.fxml is "focus traversable" by default. So we end up with an event dispatch chain in
        // which none of the nodes has the focus. Thus, KeyEvents have no target, so no bubbling of
        // KeyEvents occurs, and our KeyEvent handler in Controller never gets called.
        //
        // Once I understood what's going on, I saw an obvious solution that is easy to implement
        // here in the Application's start method. It involves giving the root node focus. I have
        // also experimented with making the paddle (a Rectangle) and the ball (a Circle) focus-traversable
        // in Controller.initialize, and those work, too. We just need one node in the event
        // dispatch chain to be able to have the focus, and then it receives the focus when the Stage is
        // displayed, and the bubbling of events causes all KeyEvents to land at our controller's
        // KeyEvent handler method.
        //
        // Want to play around with these ideas? Try commenting the Button object in pong.fxml in and out
        // while commenting the Solution below in and out as well.

        primaryStage.setTitle("Kinda Pong");
        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
        scene.setOnKeyPressed(controller);
        primaryStage.show();

        // Solution: once the Stage is displayed, explicitly put the focus on the root node.
        // You could, alternatively, go to Controller.initialize and do this.paddle.requestFocus().
        root.requestFocus();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

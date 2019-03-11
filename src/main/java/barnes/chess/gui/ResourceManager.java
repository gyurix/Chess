package barnes.chess.gui;

import barnes.chess.ChessLauncher;
import javafx.scene.image.Image;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ResourceManager {
  @Getter
  private static final ResourceManager instance = new ResourceManager();
  private Image icon;

  private ResourceManager() {
    if (instance != null)
      throw new RuntimeException("ResourceManager can only be instantiated once");
    loadResources();
  }

  private void loadResources() {
    icon = new Image(Objects.requireNonNull(ChessLauncher.class.getClassLoader().getResourceAsStream("icon.png")));
  }
}

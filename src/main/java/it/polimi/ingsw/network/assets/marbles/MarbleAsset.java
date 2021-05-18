package it.polimi.ingsw.network.assets.marbles;

import it.polimi.ingsw.network.assets.devcards.NetworkResource;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *  <p>Enum class for <em>Market Marbles</em> of MarketBoard<br>
 *  Each Marble but White is mapped to a different {@link NetworkResource Resource}.
 *  <ul>
 *  <li>{@link #WHITE}
 *  <li>{@link #BLUE}
 *  <li>{@link #GRAY}
 *  <li>{@link #YELLOW}
 *  <li>{@link #PURPLE}
 *  <li>{@link #RED}
 *  </ul>
 */
public enum MarbleAsset {
    /**
     * According to the official rulebook this <em>Marble</em> hasn't a mapped {@link NetworkResource Resource}. <br>
     * Any possible mapping to a resource due to dedicated leader effect is done in the MarketBoard
     */
    WHITE(Paths.get("src/main/resources/config/dummypath")),
    /**
     * This <em>Marble</em> is mapped to the {@link NetworkResource#SHIELD SHIELD} Resource
     */
    BLUE((Paths.get("src/main/resources/config/dummypath"))),

    /**
     * This <em>Marble</em> is mapped to the {@link NetworkResource#STONE STONE} Resource
     */
    GRAY((Paths.get("src/main/resources/config/dummypath"))),

    /**
     * This <em>Marble</em> is mapped to the {@link NetworkResource#GOLD GOLD} Resource
     */
    YELLOW((Paths.get("src/main/resources/config/dummypath"))),

    /**
     * This <em>Marble</em> is mapped to the {@link NetworkResource#SERVANT SERVANT} Resource
     */
    PURPLE((Paths.get("src/main/resources/config/dummypath"))),

    /**
     * This <em>Marble</em> is mapped to the {@link NetworkResource#FAITH FAITH} Resource
     */
    RED((Paths.get("src/main/resources/config/dummypath"))),

    INVALID((Paths.get("src/main/resources/config/dummypath")));


    private Path marbleAssetPath;
    private static final MarbleAsset[] vals = MarbleAsset.values();

    MarbleAsset(){}

    MarbleAsset(final Path marbleAssetPath) {
        this.marbleAssetPath = marbleAssetPath;
        }

    public Path getPath() {
        return this.marbleAssetPath;
    }

    public static MarbleAsset fromInt(int marbleNum){
        return marbleNum>vals.length|| marbleNum<0 ? INVALID: vals[marbleNum];
    }
}

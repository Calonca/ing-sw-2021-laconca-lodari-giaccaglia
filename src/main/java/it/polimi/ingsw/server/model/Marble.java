package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.Resource;

public enum Marble {

    WHITE(Resource.WHITEMARBLE),
    BLUE(Resource.SHIELD),
    GRAY(Resource.STONE),
    YELLOW(Resource.GOLD),
    PURPLE(Resource.SERVANT),
    RED(Resource.FAITH);

    private final Resource mappedResource;

    Marble(final Resource mappedResource) {
        this.mappedResource = mappedResource;
    }

    public Resource getConvertedMarble() {
        return this.mappedResource;
    }
}

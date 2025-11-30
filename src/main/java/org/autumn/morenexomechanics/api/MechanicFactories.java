package org.autumn.morenexomechanics.api;

import org.autumn.morenexomechanics.mechanics.factories.PotionAurasFactory;
import org.autumn.morenexomechanics.mechanics.factories.StatTrackFactory;
import org.autumn.morenexomechanics.mechanics.factories.SwappableEnchantsFactory;

import lombok.Getter;

import org.jetbrains.annotations.ApiStatus;

public final class MechanicFactories {

    @Getter private static SwappableEnchantsFactory swappableEnchants;
    @Getter private static PotionAurasFactory potionAuras;
    @Getter private static StatTrackFactory statTrack;

    /**
     *  WARNING: Bootstrapping the API should never be done externally.
     */
    @ApiStatus.Internal
    public static void bootstrap(SwappableEnchantsFactory swappable, PotionAurasFactory potion, StatTrackFactory stat) {
        swappableEnchants = swappable;
        potionAuras = potion;
        statTrack = stat;
    }

}

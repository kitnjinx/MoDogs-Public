package com.kitnjinx.modogs.item;

import com.kitnjinx.modogs.entity.custom.*;
import com.kitnjinx.modogs.entity.variant.*;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GenoReaderItem extends Item {
    private String LAST_GENO = null;

    public GenoReaderItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.level.isClientSide && target instanceof AbstractDog) {
            String testResult;
            if (target instanceof AlaskanMalamuteEntity) {
                testResult = alaskanMalamuteGeno((AlaskanMalamuteEntity) target);
            } else if (target instanceof AustralianShepherdEntity) {
                testResult = australianShepherdGeno((AustralianShepherdEntity) target);
            } else if (target instanceof BasenjiEntity) {
                testResult = basenjiGeno((BasenjiEntity) target);
            } else if (target instanceof BerneseMountainDogEntity) {
                testResult = berneseMountainDogGeno((BerneseMountainDogEntity) target);
            } else if (target instanceof BloodhoundEntity) {
                testResult = bloodhoundGeno((BloodhoundEntity) target);
            } else if (target instanceof BorderCollieEntity) {
                testResult = borderCollieGeno((BorderCollieEntity) target);
            } else if (target instanceof BostonTerrierEntity) {
                testResult = bostonTerrierGeno((BostonTerrierEntity) target);
            } else if (target instanceof BoxerEntity) {
                testResult = boxerGeno((BoxerEntity) target);
            } else if (target instanceof BullTerrierEntity) {
                testResult = bullTerrierGeno((BullTerrierEntity) target);
            } else if (target instanceof CardiganCorgiEntity) {
                testResult = cardiganCorgiGeno((CardiganCorgiEntity) target);
            } else if (target instanceof CKCharlesSpanielEntity) {
                testResult = cKCharlesSpanielGeno((CKCharlesSpanielEntity) target);
            } else if (target instanceof CockerSpanielEntity) {
                testResult = cockerSpanielGeno((CockerSpanielEntity) target);
            } else if (target instanceof DachshundEntity) {
                testResult = dachshundGeno((DachshundEntity) target);
            } else if (target instanceof DalmatianEntity) {
                testResult = dalmatianGeno((DalmatianEntity) target);
            } else if (target instanceof DobermanEntity) {
                testResult = dobermanGeno((DobermanEntity) target);
            } else if (target instanceof GermanShepherdEntity) {
                testResult = germanShepherdGeno((GermanShepherdEntity) target);
            } else if (target instanceof GoldenRetrieverEntity) {
                testResult = goldenRetrieverGeno((GoldenRetrieverEntity) target);
            } else if (target instanceof GreatDaneEntity) {
                testResult = greatDaneGeno((GreatDaneEntity) target);
            } else if (target instanceof GreyhoundEntity) {
                testResult = greyhoundGeno((GreyhoundEntity) target);
            } else if (target instanceof HuskyEntity) {
                testResult = huskyGeno((HuskyEntity) target);
            } else if (target instanceof ItalianGreyhoundEntity) {
                testResult = italianGreyhoundGeno((ItalianGreyhoundEntity) target);
            } else if (target instanceof LabRetrieverEntity) {
                testResult = labRetrieverGeno((LabRetrieverEntity) target);
            } else if (target instanceof MastiffEntity) {
                testResult = mastiffGeno((MastiffEntity) target);
            } else if (target instanceof MiniBullTerrierEntity) {
                testResult = miniBullTerrierGeno((MiniBullTerrierEntity) target);
            } else if (target instanceof MiniPinscherEntity) {
                testResult = miniPinscherGeno((MiniPinscherEntity) target);
            } else if (target instanceof  MiniSchnauzerEntity) {
                testResult = miniSchnauzerGeno((MiniSchnauzerEntity) target);
            } else if (target instanceof PembrokeCorgiEntity) {
                testResult = pembrokeCorgiGeno((PembrokeCorgiEntity) target);
            } else if (target instanceof PitBullEntity) {
                testResult = pitBullGeno((PitBullEntity) target);
            } else if (target instanceof PoodleEntity) {
                testResult = poodleGeno((PoodleEntity) target);
            } else if (target instanceof PugEntity) {
                testResult = pugGeno((PugEntity) target);
            } else if (target instanceof RedboneCoonhoundEntity) {
                testResult = redboneCoonhoundGeno((RedboneCoonhoundEntity) target);
            } else if (target instanceof RussellTerrierEntity) {
                testResult = russellTerrierGeno((RussellTerrierEntity) target);
            } else if (target instanceof SaintBernardEntity) {
                testResult = saintBernardGeno((SaintBernardEntity) target);
            } else if (target instanceof SchnauzerEntity) {
                testResult = schnauzerGeno((SchnauzerEntity) target);
            } else if (target instanceof ScottishTerrierEntity) {
                testResult = scottishTerrierGeno((ScottishTerrierEntity) target);
            } else if (target instanceof ShetlandSheepdogEntity) {
                testResult = shetlandSheepdogGeno((ShetlandSheepdogEntity) target);
            } else if (target instanceof ShibaInuEntity) {
                testResult = shibaInuGeno((ShibaInuEntity) target);
            } else if (target instanceof ToyPoodleEntity) {
                testResult = toyPoodleGeno((ToyPoodleEntity) target);
            } else {
                testResult = "This should not appear.";
            }
            
            String beginning;
            if (target.hasCustomName()) {
                beginning = target.getCustomName().getString() + "'s Geno: ";
            } else {
                beginning = target.getName().getString() + "'s Geno: ";
            }
            
            TextComponent output = new TextComponent(beginning + testResult);
            LAST_GENO = output.getText();
            player.sendMessage(output, player.getUUID());

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (this.LAST_GENO != null && level.isClientSide) {
            player.sendMessage(new TextComponent(LAST_GENO), player.getUUID());
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
    
    /* METHODS FOR READING A DOG'S GENO */
    private String alaskanMalamuteGeno(AlaskanMalamuteEntity dog) {
        if (dog.isRed() && dog.isSolid()) {
            if (dog.isSilver()) {
                return "bb ss - Silver Positive";
            } else {
                return "bb ss";
            }
        } else if (dog.isRed()) {
            if (dog.isSolidCarrier() && dog.isSilver()) {
                return "bb Ss - Silver Positive";
            } else if (dog.isSolidCarrier()) {
                return "bb Ss";
            } else if (dog.isSilver()) {
                return "bb SS - Silver Positive";
            } else {
                return "bb SS";
            }
        } else if (dog.isSolid() && dog.isSilver()) {
            if (dog.isRedCarrier()) {
                return "Bb ss - Silver Positive";
            } else {
                return "BB ss - Silver Positive";
            }
        } else if (dog.isSolid()) {
            if (dog.isRedCarrier()) {
                return "Bb ss";
            } else {
                return "BB ss";
            }
        } else {
            if (dog.isRedCarrier() && dog.isSolidCarrier()) {
                if (dog.isSilver()) {
                    return "Bb Ss - Silver Positive";
                } else {
                    return "Bb Ss";
                }
            } else if (dog.isRedCarrier()) {
                if (dog.isSilver()) {
                    return "Bb SS - Silver Positive";
                } else {
                    return "Bb SS";
                }
            } else if (dog.isSolidCarrier()) {
                if (dog.isSilver()) {
                    return "BB Ss - Silver Positive";
                } else {
                    return "BB Ss";
                }
            } else {
                if  (dog.isSilver()) {
                    return "BB SS - Silver Positive";
                } else {
                    return "BB SS";
                }
            }
        }
    }

    private String australianShepherdGeno(AustralianShepherdEntity dog) {
        if (dog.isRed()) {
            if (dog.isMerle()) {
                return "bb - Merle Positive";
            } else {
                return "bb";
            }
        } else if (dog.carriesRed()) {
            if (dog.isMerle()) {
                return "Bb - Merle Positive";
            } else {
                return "Bb";
            }
        } else {
            if (dog.isMerle()) {
                return "BB - Merle Positive";
            } else {
                return "BB";
            }
        }
    }

    private String basenjiGeno(BasenjiEntity dog) {
        if (dog.getVariant() == BasenjiVariant.TRICOLOR) {
            return "TT";
        } else if (dog.getVariant() == BasenjiVariant.BLACK) {
            if (dog.getCarrier() == 0) {
                return "BB";
            } else {
                return "BT";
            }
        } else {
            if (dog.getCarrier() == 0) {
                return "RR";
            } else if (dog.getCarrier() == 1) {
                return "RB";
            } else {
                return "RT";
            }
        }
    }

    private String berneseMountainDogGeno(BerneseMountainDogEntity dog) {
        if (dog.getVariant() == BerneseMountainDogVariant.TAN) {
            return "bb";
        } else if (dog.getCarrier()) {
            return "Bb";
        } else {
            return "BB";
        }
    }
    
    private String bloodhoundGeno(BloodhoundEntity dog) {
        if (dog.getVariant() == BloodhoundVariant.LIVER_TAN) {
            return "LTLT";
        } else if (dog.getVariant() == BloodhoundVariant.BLACK_TAN) {
            if (dog.getCarrier() == 0) {
                return "BTBT";
            } else {
                return "BTLT";
            }
        } else {
            if (dog.getCarrier() == 0) {
                return "RR";
            } else if (dog.getCarrier() == 1) {
                return "RBT";
            } else {
                return "RLT";
            }
        }
    }

    private String borderCollieGeno(BorderCollieEntity dog) {
        if (dog.getBaseColor() == 2 && dog.isRed()) {
            if (dog.isMerle()) {
                return "bb dd - Merle Positive";
            } else {
                return "bb dd";
            }
        } else if (dog.getBaseColor() == 2) {
            if (dog.isMerle() && dog.getRedCarrier()) {
                return "Bb dd - Merle Positive";
            } else if (dog.isMerle()) {
                return "BB dd - Merle Positive";
            } else if (dog.getRedCarrier()) {
                return "Bb dd";
            } else {
                return "BB dd";
            }
        } else if (dog.isRed()) {
            if (dog.isMerle() && dog.getLilacCarrier()) {
                return "bb Dd - Merle Positive";
            } else if (dog.isMerle()) {
                return "bb DD - Merle Positive";
            } else if (dog.getLilacCarrier()) {
                return "bb Dd";
            } else {
                return "bb DD";
            }
        } else if (dog.isMerle()) {
            if (dog.getLilacCarrier() && dog.getRedCarrier()) {
                return "Bb Dd - Merle Positive";
            } else if (dog.getLilacCarrier()) {
                return "BB Dd - Merle Positive";
            } else if (dog.getRedCarrier()) {
                return "Bb DD - Merle Positive";
            } else {
                return "BB DD - Merle Positive";
            }
        } else {
            if (dog.getLilacCarrier() && dog.getRedCarrier()) {
                return "Bb Dd";
            } else if (dog.getLilacCarrier()) {
                return "BB Dd";
            } else if (dog.getRedCarrier()) {
                return "Bb DD";
            } else {
                return "BB DD";
            }
        }
    }

    private String bostonTerrierGeno(BostonTerrierEntity dog) {
        if (dog.getVariant() == BostonTerrierVariant.SEAL) {
            return "bb";
        } else if (dog.isCarrier()) {
            return "Bb";
        } else {
            return "BB";
        }
    }

    private String boxerGeno(BoxerEntity dog) {
        if (dog.getVariant() == BoxerVariant.BLACK) {
            if (dog.getShade() == 1) {
                return "bb SS";
            } else if (dog.getShade() == 0) {
                return "bb Ss";
            } else {
                return "bb ss";
            }
        } else if (dog.getCarrier()) {
            if (dog.getShade() == 1) {
                return "Bb SS";
            } else if (dog.getShade() == 0) {
                return "Bb Ss";
            } else {
                return "Bb ss";
            }
        } else {
            if (dog.getShade() == 1) {
                return "BB SS";
            } else if (dog.getShade() == 0) {
                return "BB Ss";
            } else {
                return "BB ss";
            }
        }
    }

    private String bullTerrierGeno(BullTerrierEntity dog) {
        if (dog.getVariant() == BullTerrierVariant.WHITE) {
            if (dog.isRed()) {
                return "bb WW";
            } else if (dog.carriesRed()) {
                return "Bb WW";
            } else {
                return "BB WW";
            }
        } else if (dog.hasHighWhite()) {
            if (dog.isRed()) {
                return "bb Ww";
            } else if (dog.carriesRed()) {
                return "Bb Ww";
            } else {
                return "BB Ww";
            }
        } else {
            if (dog.isRed()) {
                return "bb ww";
            } else if (dog.carriesRed()) {
                return "Bb ww";
            } else {
                return "BB ww";
            }
        }
    }

    private String cardiganCorgiGeno(CardiganCorgiEntity dog) {
        if (dog.getBaseColor() == 0) {
            if (dog.isMerle()) {
                return "Bb - Merle Positive";
            } else {
                return "Bb";
            }
        } else if (dog.getBaseColor() == 1) {
            if (dog.isMerle()) {
                return "BB - Merle Positive";
            } else {
                return "BB";
            }
        } else {
            if (dog.isMerle()) {
                return "bb - Merle Positive";
            } else {
                return "bb";
            }
        }
    }

    private String cKCharlesSpanielGeno(CKCharlesSpanielEntity dog) {
        if (dog.hasPoints()) {
            if (dog.hasWhite()) {
                return "pp - White Positive";
            } else {
                return "pp";
            }
        } else if (dog.isCarrier()) {
            if (dog.hasWhite()) {
                return "Pp - White Positive";
            } else {
                return "Pp";
            }
        } else {
            if (dog.hasWhite()) {
                return "PP - White Positive";
            } else {
                return "PP";
            }
        }
    }

    private String cockerSpanielGeno(CockerSpanielEntity dog) {
        if (dog.getVariant() == CockerSpanielVariant.BUFF || dog.getVariant() == CockerSpanielVariant.SILVER) {
            if (dog.isBlack()) {
                return "bb dd";
            } else if (dog.carriesBlack()) {
                return "Bb dd";
            } else {
                return "BB dd";
            }
        } else if (dog.carriesDilute()) {
            if (dog.isBlack()) {
                return "bb Dd";
            } else if (dog.carriesBlack()) {
                return "Bb Dd";
            } else {
                return "BB Dd";
            }
        } else {
            if (dog.isBlack()) {
                return "bb DD";
            } else if (dog.carriesBlack()) {
                return "Bb DD";
            } else {
                return "BB DD";
            }
        }
    }

    private String dachshundGeno(DachshundEntity dog) {
        if (dog.isFawn() && dog.isChocolate()) {
            if (dog.isCream()) {
                return "bb dd cc";
            } else if (dog.getCreamCarrier()) {
                return "bb dd Cc";
            } else {
                return "bb dd CC";
            }
        } else if (dog.isFawn() && dog.getChocolateCarrier()) {
            if (dog.isCream()) {
                return "Bb dd cc";
            } else if (dog.getCreamCarrier()) {
                return "Bb dd Cc";
            } else {
                return "Bb dd CC";
            }
        } else if (dog.isFawn()) {
            if (dog.isCream()) {
                return "BB dd cc";
            } else if (dog.getCreamCarrier()) {
                return "BB dd Cc";
            } else {
                return "BB dd CC";
            }
        } else if (dog.isChocolate() && dog.getFawnCarrier()) {
            if (dog.isCream()) {
                return "bb Dd cc";
            } else if (dog.getCreamCarrier()) {
                return "bb Dd Cc";
            } else {
                return "bb Dd CC";
            }
        } else if (dog.isChocolate()) {
            if (dog.isCream()) {
                return "bb DD cc";
            } else if (dog.getCreamCarrier()) {
                return "bb DD Cc";
            } else {
                return "bb DD CC";
            }
        } else if (dog.getFawnCarrier() && dog.getChocolateCarrier()) {
            if (dog.isCream()) {
                return "Bb Dd cc";
            } else if (dog.getCreamCarrier()) {
                return "Bb Dd Cc";
            } else {
                return "Bb Dd CC";
            }
        } else if (dog.getFawnCarrier()) {
            if (dog.isCream()) {
                return "BB Dd cc";
            } else if (dog.getCreamCarrier()) {
                return "BB Dd Cc";
            } else {
                return "BB Dd CC";
            }
        } else if (dog.getChocolateCarrier()) {
            if (dog.isCream()) {
                return "Bb DD cc";
            } else if (dog.getCreamCarrier()) {
                return "Bb DD Cc";
            } else {
                return "Bb DD CC";
            }
        } else {
            if (dog.isCream()) {
                return "BB DD cc";
            } else if (dog.getCreamCarrier()) {
                return "BB DD Cc";
            } else {
                return "BB DD CC";
            }
        }
    }

    private String dalmatianGeno(DalmatianEntity dog) {
        if (dog.getVariant() == DalmatianVariant.BROWN) {
            return "bb";
        } else if (dog.getCarrier()) {
            return "Bb";
        } else {
            return "BB";
        }
    }

    private String dobermanGeno(DobermanEntity dog) {
        if (dog.isRed()) {
            if (dog.isDilute()) {
                return "bb dd";
            } else if (dog.carriesDilute()) {
                return "bb Dd";
            } else {
                return "bb DD";
            }
        } else if (dog.carriesRed()) {
            if (dog.isDilute()) {
                return "Bb dd";
            } else if (dog.carriesDilute()) {
                return "Bb Dd";
            } else {
                return "Bb DD";
            }
        } else {
            if (dog.isDilute()) {
                return "BB dd";
            } else if (dog.carriesDilute()) {
                return "BB Dd";
            } else {
                return "BB DD";
            }
        }
    }

    private String germanShepherdGeno(GermanShepherdEntity dog) {
        if (dog.getVariant() == GermanShepherdVariant.WHITE) {
            if (dog.getBlackDegree() == 0) {
                return "bb SS";
            } else if (dog.getBlackDegree() == 1) {
                return "bb Ss";
            } else {
                return "bb ss";
            }
        } else if (dog.getCarrier()) {
            if (dog.getBlackDegree() == 0) {
                return "Bb SS";
            } else if (dog.getBlackDegree() == 1) {
                return "Bb Ss";
            } else {
                return "Bb ss";
            }
        } else {
            if (dog.getBlackDegree() == 0) {
                return "BB SS";
            } else if (dog.getBlackDegree() == 1) {
                return "BB Ss";
            } else {
                return "BB ss";
            }
        }
    }

    private String goldenRetrieverGeno(GoldenRetrieverEntity dog) {
        if (dog.getVariant() == GoldenRetrieverVariant.LIGHT) {
            return "SS";
        } else if (dog.getVariant() == GoldenRetrieverVariant.MEDIUM) {
            return "Ss";
        } else {
            return "ss";
        }
    }

    private String greatDaneGeno(GreatDaneEntity dog) {
        if (dog.getVariant() == GreatDaneVariant.FAWN) {
            if (dog.isBlue()) {
                return "bb dd";
            } else if (dog.isBlueCarrier()) {
                return "bb Dd";
            } else {
                return "bb DD";
            }
        } else if (dog.isFawnCarrier()) {
            if (dog.isBlue()) {
                return "Bb dd";
            } else if (dog.isBlueCarrier()) {
                return "Bb Dd";
            } else {
                return "Bb DD";
            }
        } else {
            if (dog.isBlue()) {
                return "BB dd";
            } else if (dog.isBlueCarrier()) {
                return "BB Dd";
            } else {
                return "BB DD";
            }
        }
    }

    private String greyhoundGeno(GreyhoundEntity dog) {
        if (dog.getVariant() == GreyhoundVariant.WHITE && dog.isBlue()) {
            if (dog.isRed()) {
                return "bb dd WW";
            } else if (dog.isRedCarrier()) {
                return "Bb dd WW";
            } else {
                return "BB dd WW";
            }
        } else if (dog.getVariant() == GreyhoundVariant.WHITE && dog.isBlueCarrier()) {
            if (dog.isRed()) {
                return "bb Dd WW";
            } else if (dog.isRedCarrier()) {
                return "Bb Dd WW";
            } else {
                return "BB Dd WW";
            }
        } else if (dog.getVariant() == GreyhoundVariant.WHITE) {
            if (dog.isRed()) {
                return "bb DD WW";
            } else if (dog.isRedCarrier()) {
                return "Bb DD WW";
            } else {
                return "BB DD WW";
            }
        } else if (dog.hasWhite() && dog.isBlue()) {
            if (dog.isRed()) {
                return "bb dd Ww";
            } else if (dog.isRedCarrier()) {
                return "Bb dd Ww";
            } else {
                return "BB dd Ww";
            }
        } else if (dog.hasWhite() && dog.isBlueCarrier()) {
            if (dog.isRed()) {
                return "bb Dd Ww";
            } else if (dog.isRedCarrier()) {
                return "Bb Dd Ww";
            } else {
                return "BB Dd Ww";
            }
        } else if (dog.hasWhite()) {
            if (dog.isRed()) {
                return "bb DD Ww";
            } else if (dog.isRedCarrier()) {
                return "Bb DD Ww";
            } else {
                return "BB DD Ww";
            }
        } else if (dog.isBlue()) {
            if (dog.isRed()) {
                return "bb dd ww";
            } else if (dog.isRedCarrier()) {
                return "Bb dd ww";
            } else {
                return "BB dd ww";
            }
        } else if (dog.isBlueCarrier()) {
            if (dog.isRed()) {
                return "bb Dd ww";
            } else if (dog.isRedCarrier()) {
                return "Bb Dd ww";
            } else {
                return "BB Dd ww";
            }
        } else {
            if (dog.isRed()) {
                return "bb DD ww";
            } else if (dog.isRedCarrier()) {
                return "Bb DD ww";
            } else {
                return "BB DD ww";
            }
        }
    }

    private String huskyGeno(HuskyEntity dog) {
        if (dog.isRed()) {
            if (dog.isSolid()) {
               if (dog.isWhite()) {
                   return "bb ss ww";
               } else if (dog.carriesWhite()) {
                   return "bb ss Ww";
               } else {
                   return "bb ss WW";
               }
            } else if (dog.carriesSolid()) {
                if (dog.isWhite()) {
                    return "bb Ss ww";
                } else if (dog.carriesWhite()) {
                    return "bb Ss Ww";
                } else {
                    return "bb Ss WW";
                }
            } else {
                if (dog.isWhite()) {
                    return "bb SS ww";
                } else if (dog.carriesWhite()) {
                    return "bb SS Ww";
                } else {
                    return "bb SS WW";
                }
            }
        } else if (dog.carriesRed()) {
            if (dog.isSolid()) {
                if (dog.isWhite()) {
                    return "Bb ss ww";
                } else if (dog.carriesWhite()) {
                    return "Bb ss Ww";
                } else {
                    return "Bb ss WW";
                }
            } else if (dog.carriesSolid()) {
                if (dog.isWhite()) {
                    return "Bb Ss ww";
                } else if (dog.carriesWhite()) {
                    return "Bb Ss Ww";
                } else {
                    return "Bb Ss WW";
                }
            } else {
                if (dog.isWhite()) {
                    return "Bb SS ww";
                } else if (dog.carriesWhite()) {
                    return "Bb SS Ww";
                } else {
                    return "Bb SS WW";
                }
            }
        } else {
            if (dog.isSolid()) {
                if (dog.isWhite()) {
                    return "BB ss ww";
                } else if (dog.carriesWhite()) {
                    return "BB ss Ww";
                } else {
                    return "BB ss WW";
                }
            } else if (dog.carriesSolid()) {
                if (dog.isWhite()) {
                    return "BB Ss ww";
                } else if (dog.carriesWhite()) {
                    return "BB Ss Ww";
                } else {
                    return "BB Ss WW";
                }
            } else {
                if (dog.isWhite()) {
                    return "BB SS ww";
                } else if (dog.carriesWhite()) {
                    return "BB SS Ww";
                } else {
                    return "BB SS WW";
                }
            }
        }
    }

    private String italianGreyhoundGeno(ItalianGreyhoundEntity dog) {
        if (dog.isBlue()) {
            if (dog.isFawn()) {
                if (dog.hasWhite()) {
                    return "bb dd - White Positive";
                } else {
                    return "bb dd";
                }
            } else if (dog.carriesFawn()) {
                if (dog.hasWhite()) {
                    return "Bb dd - White Positive";
                } else {
                    return "Bb dd";
                }
            } else {
                if (dog.hasWhite()) {
                    return "BB dd - White Positive";
                } else {
                    return "BB dd";
                }
            }
        } else if (dog.carriesBlue()) {
            if (dog.isFawn()) {
                if (dog.hasWhite()) {
                    return "bb Dd - White Positive";
                } else {
                    return "bb Dd";
                }
            } else if (dog.carriesFawn()) {
                if (dog.hasWhite()) {
                    return "Bb Dd - White Positive";
                } else {
                    return "Bb Dd";
                }
            } else {
                if (dog.hasWhite()) {
                    return "BB Dd - White Positive";
                } else {
                    return "BB Dd";
                }
            }
        } else {
            if (dog.isFawn()) {
                if (dog.hasWhite()) {
                    return "bb DD - White Positive";
                } else {
                    return "bb DD";
                }
            } else if (dog.carriesFawn()) {
                if (dog.hasWhite()) {
                    return "Bb DD - White Positive";
                } else {
                    return "Bb DD";
                }
            } else {
                if (dog.hasWhite()) {
                    return "BB DD - White Positive";
                } else {
                    return "BB DD";
                }
            }
        }
    }

    private String labRetrieverGeno(LabRetrieverEntity dog) {
        if (dog.getVariant() == LabRetrieverVariant.YELLOW) {
            if (dog.isChocolate()) {
                return "bb ee";
            } else if (dog.getChocolateCarrier()) {
                return "Bb ee";
            } else {
                return "BB ee";
            }
        } else if (dog.getYellowCarrier()) {
            if (dog.isChocolate()) {
                return "bb Ee";
            } else if (dog.getChocolateCarrier()) {
                return "Bb Ee";
            } else {
                return "BB Ee";
            }
        } else {
            if (dog.isChocolate()) {
                return "bb EE";
            } else if (dog.getChocolateCarrier()) {
                return "Bb EE";
            } else {
                return "BB EE";
            }
        }
    }

    private String mastiffGeno(MastiffEntity dog) {
        if (dog.getVariant() == MastiffVariant.APRICOT) {
            return "ss";
        } else if (dog.isCarrier()) {
            return "Ss";
        } else {
            return "SS";
        }
    }

    private String miniBullTerrierGeno(MiniBullTerrierEntity dog) {
        if (dog.getVariant() == BullTerrierVariant.WHITE) {
            if (dog.isRed()) {
                return "bb WW";
            } else if (dog.carriesRed()) {
                return "Bb WW";
            } else {
                return "BB WW";
            }
        } else if (dog.hasHighWhite()) {
            if (dog.isRed()) {
                return "bb Ww";
            } else if (dog.carriesRed()) {
                return "Bb Ww";
            } else {
                return "BB Ww";
            }
        } else {
            if (dog.isRed()) {
                return "bb ww";
            } else if (dog.carriesRed()) {
                return "Bb ww";
            } else {
                return "BB ww";
            }
        }
    }

    private String miniPinscherGeno(MiniPinscherEntity dog) {
        if (dog.isBlack()) {
            if (dog.isDilute()) {
                return "bb dd";
            } else if (dog.carriesDilute()) {
                return "bb Dd";
            } else {
                return "bb DD";
            }
        } else if (dog.carriesBlack()) {
            if (dog.isDilute()) {
                return "Bb dd";
            } else if (dog.carriesDilute()) {
                return "Bb Dd";
            } else {
                return "Bb DD";
            }
        } else {
            if (dog.isDilute()) {
                return "BB dd";
            } else if (dog.carriesDilute()) {
                return "BB Dd";
            } else {
                return "BB DD";
            }
        }
    }

    private String miniSchnauzerGeno(MiniSchnauzerEntity dog) {
        if (dog.getVariant() == MiniSchnauzerVariant.PEPPER_SALT) {
            return "dd";
        } else if (dog.getVariant() == MiniSchnauzerVariant.BLACK_SILVER) {
            return "Dd";
        } else {
            return "DD";
        }
    }

    private String pembrokeCorgiGeno(PembrokeCorgiEntity dog) {
        if (dog.getBaseColor() == 2) {
            return "BHBH";
        } else if (dog.getBaseColor() == 1) {
            if (dog.getCarriedColor() == 2) {
                return "RHBH";
            } else {
                return "RHRH";
            }
        } else if (dog.getCarriedColor() == 1) {
            return "RRH";
        } else if (dog.getCarriedColor() == 2) {
            return "RBH";
        } else {
            return "RR";
        }
    }

    private String pitBullGeno(PitBullEntity dog) {
        if (dog.getVariant() == PitBullVariant.WHITE && dog.isBlue()) {
            if (dog.isBrown()) {
                return "bb dd WW";
            } else if (dog.isBrownCarrier()) {
                return "Bb dd WW";
            } else {
                return "BB dd WW";
            }
        } else if (dog.getVariant() == PitBullVariant.WHITE && dog.isBlueCarrier()) {
            if (dog.isBrown()) {
                return "bb Dd WW";
            } else if (dog.isBrownCarrier()) {
                return "Bb Dd WW";
            } else {
                return "BB Dd WW";
            }
        } else if (dog.getVariant() == PitBullVariant.WHITE) {
            if (dog.isBrown()) {
                return "bb DD WW";
            } else if (dog.isBrownCarrier()) {
                return "Bb DD WW";
            } else {
                return "BB DD WW";
            }
        } else if (dog.hasWhite() && dog.isBlue()) {
            if (dog.isBrown()) {
                return "bb dd Ww";
            } else if (dog.isBrownCarrier()) {
                return "Bb dd Ww";
            } else {
                return "BB dd Ww";
            }
        } else if (dog.hasWhite() && dog.isBlueCarrier()) {
            if (dog.isBrown()) {
                return "bb Dd Ww";
            } else if (dog.isBrownCarrier()) {
                return "Bb Dd Ww";
            } else {
                return "BB Dd Ww";
            }
        } else if (dog.hasWhite()) {
            if (dog.isBrown()) {
                return "bb DD Ww";
            } else if (dog.isBrownCarrier()) {
                return "Bb DD Ww";
            } else {
                return "BB DD Ww";
            }
        } else if (dog.isBlue()) {
            if (dog.isBrown()) {
                return "bb dd ww";
            } else if (dog.isBrownCarrier()) {
                return "Bb dd ww";
            } else {
                return "BB dd ww";
            }
        } else if (dog.isBlueCarrier()) {
            if (dog.isBrown()) {
                return "bb Dd ww";
            } else if (dog.isBrownCarrier()) {
                return "Bb Dd ww";
            } else {
                return "BB Dd ww";
            }
        } else {
            if (dog.isBrown()) {
                return "bb DD ww";
            } else if (dog.isBrownCarrier()) {
                return "Bb DD ww";
            } else {
                return "BB DD ww";
            }
        }
    }

    private String poodleGeno(PoodleEntity dog) {
        if (dog.getVariant() == PoodleVariant.WHITE) {
            if (dog.isBrown()) {
                if (dog.isDilute()) {
                    return "bb dd ww";
                } else if (dog.carriesDilute()) {
                    return "bb Dd ww";
                } else {
                    return "bb DD ww";
                }
            } else if (dog.carriesBrown()) {
                if (dog.isDilute()) {
                    return "Bb dd ww";
                } else if (dog.carriesDilute()) {
                    return "Bb Dd ww";
                } else {
                    return "Bb DD ww";
                }
            } else {
                if (dog.isDilute()) {
                    return "BB dd ww";
                } else if (dog.carriesDilute()) {
                    return "BB Dd ww";
                } else {
                    return "BB DD ww";
                }
            }
        } else if (dog.carriesWhite()) {
            if (dog.isBrown()) {
                if (dog.isDilute()) {
                    return "bb dd Ww";
                } else if (dog.carriesDilute()) {
                    return "bb Dd Ww";
                } else {
                    return "bb DD Ww";
                }
            } else if (dog.carriesBrown()) {
                if (dog.isDilute()) {
                    return "Bb dd Ww";
                } else if (dog.carriesDilute()) {
                    return "Bb Dd Ww";
                } else {
                    return "Bb DD Ww";
                }
            } else {
                if (dog.isDilute()) {
                    return "BB dd Ww";
                } else if (dog.carriesDilute()) {
                    return "BB Dd Ww";
                } else {
                    return "BB DD Ww";
                }
            }
        } else {
            if (dog.isBrown()) {
                if (dog.isDilute()) {
                    return "bb dd WW";
                } else if (dog.carriesDilute()) {
                    return "bb Dd WW";
                } else {
                    return "bb DD WW";
                }
            } else if (dog.carriesBrown()) {
                if (dog.isDilute()) {
                    return "Bb dd WW";
                } else if (dog.carriesDilute()) {
                    return "Bb Dd WW";
                } else {
                    return "Bb DD WW";
                }
            } else {
                if (dog.isDilute()) {
                    return "BB dd WW";
                } else if (dog.carriesDilute()) {
                    return "BB Dd WW";
                } else {
                    return "BB DD WW";
                }
            }
        }
    }

    private String pugGeno(PugEntity dog) {
        if (dog.getVariant() == PugVariant.BLACK) {
            return "bb";
        } else if (dog.isCarrier()) {
            return "Bb";
        } else {
            return "BB";
        }
    }

    private String redboneCoonhoundGeno(RedboneCoonhoundEntity dog) {
        if (dog.getVariant() == RedboneCoonhoundVariant.BROWN) {
            return "SS";
        } else if (dog.getVariant() == RedboneCoonhoundVariant.RED) {
            return "Ss";
        } else {
            return "ss";
        }
    }

    private String russellTerrierGeno(RussellTerrierEntity dog) {
        if (dog.isTricolor() && dog.isTan()) {
            if (dog.getIntensity() == 1) {
                return "kyky atat II";
            } else if (dog.getIntensity() == 2) {
                return "kyky atat Ii";
            } else {
                return "kyky atat ii";
            }
        } else if (dog.isTricolor() && dog.isTanCarrier()) {
            if (dog.getIntensity() == 1) {
                return "Kbky atat II";
            } else if (dog.getIntensity() == 2) {
                return "Kbky atat Ii";
            } else {
                return "Kbky atat ii";
            }
        } else if (dog.isTricolor()) {
            if (dog.getIntensity() == 1) {
                return "KbKb atat II";
            } else if (dog.getIntensity() == 2) {
                return "KbKb atat Ii";
            } else {
                return "KbKb atat ii";
            }
        } else if (dog.isTricolorCarrier() && dog.isTan()) {
            if (dog.getIntensity() == 1) {
                return "kyky Ayat II";
            } else if (dog.getIntensity() == 2) {
                return "kyky Ayat Ii";
            } else {
                return "kyky Ayat ii";
            }
        } else if (dog.isTricolorCarrier() && dog.isTanCarrier()) {
            if (dog.getIntensity() == 1) {
                return "Kbky Ayat II";
            } else if (dog.getIntensity() == 2) {
                return "Kbky Ayat Ii";
            } else {
                return "Kbky Ayat ii";
            }
        } else if (dog.isTricolorCarrier()) {
            if (dog.getIntensity() == 1) {
                return "KbKb Ayat II";
            } else if (dog.getIntensity() == 2) {
                return "KbKb Ayat Ii";
            } else {
                return "KbKb Ayat ii";
            }
        } else if (dog.isTan()) {
            if (dog.getIntensity() == 1) {
                return "kyky AyAy II";
            } else if (dog.getIntensity() == 2) {
                return "kyky AyAy Ii";
            } else {
                return "kyky AyAy ii";
            }
        } else if (dog.isTanCarrier()) {
            if (dog.getIntensity() == 1) {
                return "Kbky AyAy II";
            } else if (dog.getIntensity() == 2) {
                return "Kbky AyAy Ii";
            } else {
                return "Kbky AyAy ii";
            }
        } else {
            if (dog.getIntensity() == 1) {
                return "KbKb AyAy II";
            } else if (dog.getIntensity() == 2) {
                return "KbKb AyAy Ii";
            } else {
                return "KbKb AyAy ii";
            }
        }
    }

    private String saintBernardGeno(SaintBernardEntity dog) {
        if (dog.getShade() == 0) {
            if (dog.isGolden()) {
                return "gg SS";
            } else if (dog.isGoldenCarrier()) {
                return "Gg SS";
            } else {
                return "GG SS";
            }
        } else if (dog.getShade() == 1) {
            if (dog.isGolden()) {
                return "gg Ss";
            } else if (dog.isGoldenCarrier()) {
                return "Gg Ss";
            } else {
                return "GG Ss";
            }
        } else {
            if (dog.isGolden()) {
                return "gg ss";
            } else if (dog.isGoldenCarrier()) {
                return "Gg ss";
            } else {
                return "GG ss";
            }
        }
    }

    private String schnauzerGeno(SchnauzerEntity dog) {
        if (dog.getVariant() == SchnauzerVariant.PEPPER_SALT) {
            return "dd";
        } else if (dog.getCarrier()) {
            return "Dd";
        } else {
            return "DD";
        }
    }

    private String scottishTerrierGeno(ScottishTerrierEntity dog) {
        if (dog.getVariant() == ScottishTerrierVariant.WHEATEN) {
            return "dd";
        } else if (dog.isCarrier()) {
            return "Dd";
        } else {
            return "DD";
        }
    }

    private String shetlandSheepdogGeno(ShetlandSheepdogEntity dog) {
        if (dog.isSable() && dog.isTan()) {
            if (dog.hasMerle()) {
                return "bb pp - Merle Positive";
            } else {
                return "bb pp";
            }
        } else if (dog.isSable() && dog.carriesTan()) {
            if (dog.hasMerle()) {
                return "bb Pp - Merle Positive";
            } else {
                return "bb Pp";
            }
        } else if (dog.isSable()) {
            if (dog.hasMerle()) {
                return "bb PP - Merle Positive";
            } else {
                return "bb PP";
            }
        } else if (dog.isTan() && dog.carriesSable()) {
            if (dog.hasMerle()) {
                return "Bb pp - Merle Positive";
            } else {
                return "Bb pp";
            }
        } else if (dog.isTan()) {
            if (dog.hasMerle()) {
                return "BB pp - Merle Positive";
            } else {
                return "BB pp";
            }
        } else if (dog.carriesSable() && dog.carriesTan()) {
            if (dog.hasMerle()) {
                return "Bb Pp - Merle Positive";
            } else {
                return "Bb Pp";
            }
        } else if (dog.carriesSable()) {
            if (dog.hasMerle()) {
                return "Bb PP - Merle Positive";
            } else {
                return "Bb PP";
            }
        } else if (dog.carriesTan()) {
            if (dog.hasMerle()) {
                return "BB Pp - Merle Positive";
            } else {
                return "BB Pp";
            }
        } else {
            if (dog.hasMerle()) {
                return "BB PP - Merle Positive";
            } else {
                return "BB PP";
            }
        }
    }

    private String shibaInuGeno(ShibaInuEntity dog) {
        if (dog.isCream()) {
            if (dog.isBlack()) {
                return "bb dd";
            } else if (dog.carriesBlack()) {
                return "Bb dd";
            } else {
                return "BB dd";
            }
        } else if (dog.carriesCream()) {
            if (dog.isBlack()) {
                return "bb Dd";
            } else if (dog.carriesBlack()) {
                return "Bb Dd";
            } else {
                return "BB Dd";
            }
        } else {
            if (dog.isBlack()) {
                return "bb DD";
            } else if (dog.carriesBlack()) {
                return "Bb DD";
            } else {
                return "BB DD";
            }
        }
    }

    private String toyPoodleGeno(ToyPoodleEntity dog) {
        if (dog.getVariant() == PoodleVariant.WHITE) {
            if (dog.isBrown()) {
                if (dog.isDilute()) {
                    return "bb dd ww";
                } else if (dog.carriesDilute()) {
                    return "bb Dd ww";
                } else {
                    return "bb DD ww";
                }
            } else if (dog.carriesBrown()) {
                if (dog.isDilute()) {
                    return "Bb dd ww";
                } else if (dog.carriesDilute()) {
                    return "Bb Dd ww";
                } else {
                    return "Bb DD ww";
                }
            } else {
                if (dog.isDilute()) {
                    return "BB dd ww";
                } else if (dog.carriesDilute()) {
                    return "BB Dd ww";
                } else {
                    return "BB DD ww";
                }
            }
        } else if (dog.carriesWhite()) {
            if (dog.isBrown()) {
                if (dog.isDilute()) {
                    return "bb dd Ww";
                } else if (dog.carriesDilute()) {
                    return "bb Dd Ww";
                } else {
                    return "bb DD Ww";
                }
            } else if (dog.carriesBrown()) {
                if (dog.isDilute()) {
                    return "Bb dd Ww";
                } else if (dog.carriesDilute()) {
                    return "Bb Dd Ww";
                } else {
                    return "Bb DD Ww";
                }
            } else {
                if (dog.isDilute()) {
                    return "BB dd Ww";
                } else if (dog.carriesDilute()) {
                    return "BB Dd Ww";
                } else {
                    return "BB DD Ww";
                }
            }
        } else {
            if (dog.isBrown()) {
                if (dog.isDilute()) {
                    return "bb dd WW";
                } else if (dog.carriesDilute()) {
                    return "bb Dd WW";
                } else {
                    return "bb DD WW";
                }
            } else if (dog.carriesBrown()) {
                if (dog.isDilute()) {
                    return "Bb dd WW";
                } else if (dog.carriesDilute()) {
                    return "Bb Dd WW";
                } else {
                    return "Bb DD WW";
                }
            } else {
                if (dog.isDilute()) {
                    return "BB dd WW";
                } else if (dog.carriesDilute()) {
                    return "BB Dd WW";
                } else {
                    return "BB DD WW";
                }
            }
        }
    }
}

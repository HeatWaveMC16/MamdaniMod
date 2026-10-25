package com.mamdanimod;

import com.mojang.logging.LogUtils;
import com.mamdanimod.entity.BigYahuEntity;
import com.mamdanimod.entity.BigYahuRenderer;
import com.mamdanimod.entity.MamdaniEntity;
import com.mamdanimod.entity.MamdaniRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MamdaniMod.MODID)
public final class MamdaniMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "mamdanimod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Items which will all be registered under the "mamdanimod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "mamdanimod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<MamdaniEntity>> MAMDANI = ENTITY_TYPES.register("mamdani",
        () -> EntityType.Builder.of(MamdaniEntity::new, MobCategory.CREATURE)
            .sized(0.6F, 1.8F)
            .build(ENTITY_TYPES.key("mamdani"))
    );
    public static final RegistryObject<EntityType<BigYahuEntity>> BIG_YAHU = ENTITY_TYPES.register("big_yahu",
        () -> EntityType.Builder.of(BigYahuEntity::new, MobCategory.MONSTER)
            .sized(0.6F, 1.8F)
            .build(ENTITY_TYPES.key("big_yahu"))
    );
    public static final RegistryObject<Item> MAMDANI_SPAWN_EGG = ITEMS.register("mamdani_spawn_egg",
        () -> new SpawnEggItem(new Item.Properties()
            .setId(ITEMS.key("mamdani_spawn_egg"))
            .spawnEgg(MAMDANI.get())
        )
    );
    public static final RegistryObject<Item> BIG_YAHU_SPAWN_EGG = ITEMS.register("big_yahu_spawn_egg",
        () -> new SpawnEggItem(new Item.Properties()
            .setId(ITEMS.key("big_yahu_spawn_egg"))
            .spawnEgg(BIG_YAHU.get())
        )
    );
    public static final RegistryObject<Item> MAMDANIMOD_TAB_ICON = ITEMS.register("mamdanimod_tab_icon",
        () -> new Item(new Item.Properties().setId(ITEMS.key("mamdanimod_tab_icon")))
    );

    // The icon-only item keeps the Statue of Liberty artwork out of the block and item lists.
    public static final RegistryObject<CreativeModeTab> MAMDANIMOD_TAB = CREATIVE_MODE_TABS.register("mamdanimod_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable("itemGroup.mamdanimod.mamdanimod_tab"))
            .icon(() -> MAMDANIMOD_TAB_ICON.get().getDefaultInstance())
            .displayItems((_, output) -> {
                output.accept(MAMDANI_SPAWN_EGG.get());
                output.accept(BIG_YAHU_SPAWN_EGG.get());
            }).build());

    public MamdaniMod(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();

        // Register the commonSetup method for modloading
        FMLCommonSetupEvent.getBus(modBusGroup).addListener(this::commonSetup);

        ENTITY_TYPES.register(modBusGroup);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modBusGroup);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modBusGroup);

        // Register the item to a creative tab
        BuildCreativeModeTabContentsEvent.BUS.addListener(MamdaniMod::addCreative);
        EntityAttributeCreationEvent.BUS.addListener(event ->
        {
            event.put(MAMDANI.get(), MamdaniEntity.createAttributes().build());
            event.put(BIG_YAHU.get(), BigYahuEntity.createAttributes().build());
        }
        );

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS)
            event.accept(MAMDANI_SPAWN_EGG);
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS)
            event.accept(BIG_YAHU_SPAWN_EGG);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        static {
            EntityRenderersEvent.RegisterRenderers.BUS.addListener(event ->
            {
                event.registerEntityRenderer(MAMDANI.get(), MamdaniRenderer::new);
                event.registerEntityRenderer(BIG_YAHU.get(), BigYahuRenderer::new);
            }
            );
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}

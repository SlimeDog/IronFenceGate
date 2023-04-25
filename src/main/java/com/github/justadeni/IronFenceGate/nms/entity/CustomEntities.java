package com.github.justadeni.IronFenceGate.nms.entity;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class CustomEntities {

    // Credit: https://www.spigotmc.org/threads/persisting-a-custom-nms-villager-through-server-restarts-in-1-19-3.584683/
    public static void register(){

        // In case plugin gets reloaded
        if (BuiltInRegistries.ENTITY_TYPE.getOptional(new ResourceLocation("custom_pig")).isPresent())
            return;

        // Get entity type registry
        CraftServer server = ((CraftServer) Bukkit.getServer());
        DedicatedServer dedicatedServer = server.getServer();
        WritableRegistry<EntityType<?>> entityTypeRegistry = (WritableRegistry<EntityType<?>>)
                dedicatedServer.registryAccess().registryOrThrow(Registries.ENTITY_TYPE);

        try {
            // Unfreeze registry
            //IronFenceGate.get().log("Unfreezing entity type registry (1/2)...");
            // l = private boolean frozen
            Field frozen = MappedRegistry.class.getDeclaredField("l");
            frozen.setAccessible(true);
            frozen.set(entityTypeRegistry, false);

            //IronFenceGate.get().log("Unfreezing entity type registry (2/2)...");
            // m = private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;
            Field unregisteredHolderMap = MappedRegistry.class.getDeclaredField("m");
            unregisteredHolderMap.setAccessible(true);
            unregisteredHolderMap.set(BuiltInRegistries.ENTITY_TYPE, new HashMap<>());

            // Build entity
            EntityType.Builder<Entity> customPigBuilder = EntityType.Builder.of(CustomPig::new, MobCategory.CREATURE)
                    .sized(0.9F, 0.9F)
                    .clientTrackingRange(10);

            EntityType.Builder<Entity> customArmorstandBuilder = EntityType.Builder.of(CustomArmorstand::new, MobCategory.MISC)
                    .sized(0.25F, 0.9875F)
                    .clientTrackingRange(10);

            //IronFenceGate.get().log("Building entities");
            EntityType<Entity> custompig = customPigBuilder.build("custom_pig");
            EntityType<Entity> customarmorstand = customArmorstandBuilder.build("custom_armor_stand");

            // Create intrusive holder
            entityTypeRegistry.createIntrusiveHolder(custompig);
            entityTypeRegistry.createIntrusiveHolder(customarmorstand);
            // Register custom entity
            ResourceKey<EntityType<?>> pigKey = ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation("custom_pig"));
            entityTypeRegistry.register(pigKey, custompig, Lifecycle.stable());

            ResourceKey<EntityType<?>> armorstandKey = ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation("custom_armor_stand"));
            entityTypeRegistry.register(armorstandKey, customarmorstand, Lifecycle.stable());

            // a = private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder builder)
            Method register = EntityType.class.getDeclaredMethod("a", String.class, EntityType.Builder.class);
            register.setAccessible(true);
            register.invoke(null, "custom_pig", customPigBuilder);
            register.invoke(null, "custom_armor_stand", customArmorstandBuilder);
            // Re-freeze registry
            //IronFenceGate.get().log("Re-freezing entity type registry...");
            IronFenceGate.get().log("Please disregard these errors. We are \n aware of them and they are completely harmless.");

            frozen.set(entityTypeRegistry, true);
            unregisteredHolderMap.set(BuiltInRegistries.ENTITY_TYPE, null);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e){
            IronFenceGate.get().log("Registry error, printing stacktrace");
            e.printStackTrace();
        }
    }

}

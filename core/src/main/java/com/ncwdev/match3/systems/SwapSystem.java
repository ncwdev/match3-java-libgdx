package com.ncwdev.match3.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ncwdev.ecs.Entity;
import com.ncwdev.ecs.System;
import com.ncwdev.ecs.World;
import com.ncwdev.match3.GameConfig;
import com.ncwdev.match3.components.Position;
import com.ncwdev.match3.components.SwapAnimation;
import com.ncwdev.match3.components.Transform;
import com.ncwdev.match3.events.InputEnabledEvent;
import com.ncwdev.match3.events.SwapCompletedEvent;
import com.ncwdev.match3.events.SwapInitiatedEvent;

public class SwapSystem extends System {
    public SwapSystem(World world) {
        // Listen for SwapEvent
        world.getEventManager().addListener(SwapInitiatedEvent.class, event -> {
            // if (isValidSwap(event)) {
            //     performSwap(event);
            //     world.getEventManager().postEvent(new SwapCompletedEvent());
            // }
            if (isValidSwap(event)) {
                startSwapAnimation(event.tileA, event.tileB);
                disableInputDuringSwap();
            }
        });
    }

    @Override
    public void update(float deltaTime) {
        Array<Entity> swappingEntities = new Array<>();

        // Collect all entities with ongoing swaps
        for (Entity entity : world.getEntities()) {
            if (entity.has(SwapAnimation.class)) {
                swappingEntities.add(entity);
            }
        }

        // Update each swap animation
        for (Entity entity : swappingEntities) {
            SwapAnimation anim = entity.get(SwapAnimation.class);
            anim.elapsed += deltaTime;
            
            updateVisualPosition(entity, anim);
            
            if (anim.elapsed >= anim.duration) {
                completeSwap(entity, anim);
            }
        }
    }

    private void updateVisualPosition(Entity entity, SwapAnimation anim) {
        // Calculate interpolation progress (0-1)
        float progress = Math.min(anim.elapsed / anim.duration, 1f);
        float t = Interpolation.smoother.apply(progress);

        // Update visual position (using Transform component)
        Transform transform = entity.get(Transform.class);
        transform.x = anim.startPos.x + (anim.targetPos.x - anim.startPos.x) * t;
        transform.y = anim.startPos.y + (anim.targetPos.y - anim.startPos.y) * t;
    }

    private void completeSwap(Entity entity, SwapAnimation anim) {
        // Remove animation component
        entity.remove(SwapAnimation.class);

        // Check if both partners finished animating
        if (!anim.partner.has(SwapAnimation.class)) {
            // Finalize grid positions
            swapGridPositions(entity, anim.partner);

            // Notify about completed swap
            world.getEventManager().postEvent(new SwapCompletedEvent(entity, anim.partner));

            // Re-enable input
            enableInputAfterSwap();
        }
    }

    private void swapGridPositions(Entity a, Entity b) {
        Position posA = a.get(Position.class);
        Position posB = b.get(Position.class);
        
        // Swap grid positions
        posA.swap(posB);

        // Snap visual positions to final grid positions
        Transform transformA = a.get(Transform.class);
        transformA.x = posA.x;
        transformA.y = posA.y;

        Transform transformB = b.get(Transform.class);
        transformB.x = posB.x;
        transformB.y = posB.y;
    }

    // private void performSwap(SwapInitiatedEvent event) {
    //     Position posA = event.tileA.get(Position.class);
    //     Position posB = event.tileB.get(Position.class);
    //     posA.swap(posB);
    // }

    private boolean isValidSwap(SwapInitiatedEvent event) {
        // Check if swap is valid (adjacent tiles)
        Position posA = event.tileA.get(Position.class);
        Position posB = event.tileB.get(Position.class);

        return Math.abs(posA.x - posB.x) + Math.abs(posA.y - posB.y) == 1;
    }

    private void startSwapAnimation(Entity tileA, Entity tileB) {
        Position posA = tileA.get(Position.class);
        Position posB = tileB.get(Position.class);

        // Create animations for both tiles
        tileA.add(new SwapAnimation(tileB,
            new Vector2(posA.x, posA.y),
            new Vector2(posB.x, posB.y),
            GameConfig.SWAP_DURATION
        ));

        tileB.add(new SwapAnimation(tileA,
            new Vector2(posB.x, posB.y),
            new Vector2(posA.x, posA.y),
            GameConfig.SWAP_DURATION
        ));
    }

    private void disableInputDuringSwap() {
        world.getEventManager().postEvent(new InputEnabledEvent(false));
    }

    private void enableInputAfterSwap() {
        world.getEventManager().postEvent(new InputEnabledEvent(true));
    }
}

package de.zcience.z1.gameplay.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.zcience.z1.gameplay.input.InputComponent;
import de.zcience.z1.gameplay.movement.JumpComponent;
import de.zcience.zengine.physics.components.Box2DComponent;
import de.zcience.zengine.physics.components.PositionComponent;
import de.zcience.zengine.physics.systems.PhysicsSystem;
import de.zcience.zengine.physics.utils.PhysicsBodyDef;
import de.zcience.zengine.physics.utils.PhysicsFixtureDef;
import de.zcience.zengine.render.components.SpriteAnimationComponent;
import de.zcience.zengine.render.components.TextureComponent;
import de.zcience.zengine.render.utils.ZAnimation;

public class EntityCreator
{

    public static short LIGHT = 0x008;

    public static short WORLDOBJECT = 0x002;

    public static short PLAYER = 0x006;

    private PooledEngine engine;

    private AssetManager assetManager;

    public EntityCreator(PooledEngine engine, AssetManager assetManager)
    {
        this.engine = engine;
        this.assetManager = assetManager;
    }

    public Entity createPlayer(float x, float y)
    {
        Entity entity = engine.createEntity();
        PhysicsSystem physicsSystem = engine.getSystem(PhysicsSystem.class);

        PositionComponent pComp = engine.createComponent(PositionComponent.class);
        entity.add(pComp);

        InputComponent iComp = engine.createComponent(InputComponent.class);
        iComp.init(true, 4f, 20f);
        entity.add(iComp);

        JumpComponent jComp = new JumpComponent();
        jComp.setMaxImpulse(10.0f);
        entity.add(jComp);
        /*
         * Box2D body
         */
        float width = 1.0f;
        float height = 1.0f;
        Box2DComponent box2D = engine.createComponent(Box2DComponent.class);
        PhysicsBodyDef bodyDef = new PhysicsBodyDef(BodyType.DynamicBody, physicsSystem).fixedRotation(true).position(x, y);
        box2D.init(bodyDef, physicsSystem, entity);
//        // Head
//        PhysicsFixtureDef fixtureDef = new PhysicsFixtureDef(physicsSystem).shapeCircle(height * 0.12f, new Vector2(0, height * 0.25f)).friction(0);
//        box2D.createFixture(fixtureDef);
//        // middle
//        fixtureDef = new PhysicsFixtureDef(physicsSystem).shapeBox(width * 0.2f, height * 0.6f, new Vector2(0, -height * 0.1f), 0).friction(0);
//        box2D.createFixture(fixtureDef);
//        // bottom
//        fixtureDef = new PhysicsFixtureDef(physicsSystem).shapeCircle(height * 0.1f, new Vector2(0, -height * 0.4f));
//        box2D.createFixture(fixtureDef);
        // jumpsensor
        PhysicsFixtureDef fixtureDef = new PhysicsFixtureDef(physicsSystem).shapeCircle(height * 0.1f, new Vector2(0, -height * 0.45f)).sensor(true).mask(WORLDOBJECT);
        Fixture fixture = box2D.createFixture(fixtureDef);
        fixture.setUserData("Jump");

        fixtureDef = new PhysicsFixtureDef(physicsSystem).shapeBox(width * 0.2f, height * 0.6f, new Vector2(0, -height * 0.1f), 0).friction(0);
        box2D.createFixture(fixtureDef);

        entity.add(box2D);

        TextureAtlas atlas = assetManager.get("packedImages/Z1.atlas", TextureAtlas.class);
        ZAnimation anim = new ZAnimation(1.0f / 16.0f, atlas.findRegions("player_walk"), PlayMode.LOOP);
        SpriteAnimationComponent spriteAnim = engine.createComponent(SpriteAnimationComponent.class);
        spriteAnim.setAnimation("walk", anim);
        entity.add(spriteAnim);

        TextureComponent texComp = engine.createComponent(TextureComponent.class);
        texComp.setHeight(height);
        texComp.setWidth(width * 0.5f);
        entity.add(texComp);

        // LightComponent
        // LightComponent lightCompo = engine
        // .createComponent(LightComponent.class);
        // lightCompo.light = new PointLight(LightSystem.rayHandler, 256,
        // new Color(0.3f, 0.3f, 0.3f, 1f), 9, x, y);

        // lightCompo.light.attachToBody(physicsBody.getBody());
        //
        // entity.add(lightCompo);

        engine.addEntity(entity);
        return entity;
    }

    public PooledEngine getEngine()
    {
        return engine;
    }

    public void setEngine(PooledEngine engine)
    {
        this.engine = engine;
    }
}

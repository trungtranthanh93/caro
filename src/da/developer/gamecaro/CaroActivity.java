package da.developer.gamecaro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.animator.AlphaMenuSceneAnimator;
import org.andengine.entity.scene.menu.animator.SlideMenuSceneAnimator;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TickerText;
import org.andengine.entity.text.TickerText.TickerTextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.align.VerticalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.spatial.Direction;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.ease.EaseBounceOut;

import da.developer.gamecaro.dialog.DialogExit;
import da.developer.gamecaro.dialog.DialogSound;
import da.developer.gamecaro.dialog.DialogWin;
import da.developer.gamecaro.dialog.DialogNew;
import da.developer.gamecaro.util.UtilActivity;


import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CaroActivity extends SimpleBaseGameActivity  implements OnClickListener, IOnMenuItemClickListener  {

	Activity activity;
	private Font mFont;
	final private int CAMERA_WIDTH = 480;
	final private int CAMERA_HEIGHT = 720;
	int winner = 0;
	final private int BOARD_WIDTH = 420;
	final private int BOARD_HEIGHT = 420;
	private static Scene scene;
	private Camera camera;
	final private int GRID_WIDTH = 16;
	final private int GRID_HEIGHT = 16;

	public static ArrayList<Sprite> listSprite = new ArrayList<Sprite>(); // Mảng chứa sprite
	public static ArrayList<Node> listUndo = new ArrayList<Node>(); // Mảng chứa các Node được đánh vào trong bàn cờ
	
	int currentPlayer = 1;

	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mBlankTextureRegion;
	private ITextureRegion mXTextureRegion;
	private ITextureRegion mOTextureRegion;
	private ITextureRegion mBnBackTextureRegion;
	private ITextureRegion mBackgroundTextureRegion;
	private ITextureRegion mBnNewTextureRegion;
	private ITextureRegion mBnUndoTextureRegion;
	private ITextureRegion mSettingTextureRegion;
	private String tyso = "0 : 0";
	
	protected static final int MENU_RESET = 0;
	protected static final int MENU_QUIT = MENU_RESET + 1;
	public static final String KEY_TASK_X = "tX";
	public static final String KEY_TASK_Y = "tY";
	public static final int DIALOG_NEW_TASK_ID = 0;
	
	
	protected MenuScene mMenuScene;
	private ITexture mBackgroundTexture;
	protected ITextureRegion mFaceTextureRegion;

	protected ITextureRegion mMenuResetTextureRegion;

	protected ITextureRegion mMenuQuitTextureRegion;
	private ButtonSprite[][] gridSprite = new ButtonSprite[BOARD_WIDTH][BOARD_HEIGHT];
	VertexBufferObjectManager VBOManager;
	public static CaroModel board = new CaroModel();

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
				reset();
				board.reset();
				ValueControl.isExit = false;
				clearSprite();
				listUndo.clear();
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
	            break;
	        }
	    }
	};
	

	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		
		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}
	
	//
	public void show(final ButtonSprite pButtonSprite, int x, int y) {
	
		board.setValueX(x, y);
		pButtonSprite.setCurrentTileIndex(2);
	}
    
	@Override
	public void onClick(final ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				MainActivity.mSound.playClick();
				float x = pButtonSprite.getX();
				float y = pButtonSprite.getY();
				
				int gridX = (int)Math.floor(x/ (BOARD_WIDTH) * GRID_WIDTH);
				int gridY = (int)Math.floor(y/ (BOARD_HEIGHT) * GRID_HEIGHT);
				if (CaroBoard.BoardArr[gridX][ gridY] == 0 && ValueControl.isExit == false)
				{

					if(ValueControl.isPlayer == 1)
					{
						Node nodeUndoX = new Node();
						CaroBoard.BoardArr[gridX][gridY] = 1;
						ShowSpriteXO(gridX,gridY);
						nodeUndoX.Row = gridX;
						nodeUndoX.Column = gridY;
						listUndo.add(nodeUndoX);
						winner = CaroModel.CheckEnd(gridX, gridY);
						currentPlayer = 2;
						if(winner == 1){
							ValueControl.iCntX++;
							ValueControl.isExit = true;
							ValueControl.winLose = "Bạn đã chiến thắng!!! \n Bạn có muốn tiếp tục?";
							// Hien thi dialog win
							DialogWinLose();
						}
						
						else
						{
							CaroBoard.ComputerPlay();
							Node nodeUndoY = new Node();
							ShowSpriteXO(CaroBoard._x,CaroBoard._y);
							nodeUndoY.Row = CaroBoard._x;
							nodeUndoY.Column = CaroBoard._y;
							listUndo.add(nodeUndoY);
							winner = CaroModel.CheckEnd(CaroBoard._x,CaroBoard._y);
							currentPlayer = 1;
							if(winner == 2){
								ValueControl.iCntO++;
								ValueControl.isExit = true;
								// Hien thi dialog win
								ValueControl.winLose = "Bạn đã thua! \n Bạn có muốn tiếp tục?";
								DialogWinLose();
							}
						}
						
					}
				if(ValueControl.isPlayer == 2)
				{
					if(CaroBoard.BoardArr[gridX][ gridY] !=1 && CaroBoard.BoardArr[gridX][ gridY] !=2)
					{
						if(currentPlayer == 1)
						{
							Node nodeUndoX = new Node();
							CaroBoard.BoardArr[gridX][gridY] = 1;
							ShowSpriteXO(gridX,gridY);
							nodeUndoX.Row = gridX;
							nodeUndoX.Column = gridY;
							listUndo.add(nodeUndoX);
							currentPlayer = 2;
							winner = CaroModel.CheckEnd(gridX, gridY);
							if(winner == 1){
								ValueControl.iCntX++;
								ValueControl.isExit = true;
								// Hien thi dialog win
								ValueControl.winLose = "X đã chiến thắng! \n Bạn có muốn tiếp tục?";
								DialogWinLose();
							}
						}
						else
						{
							Node nodeUndoX = new Node();
							CaroBoard.BoardArr[gridX][gridY] = 2;
							ShowSpriteXO(gridX,gridY);
							nodeUndoX.Row = gridX;
							nodeUndoX.Column = gridY;
							listUndo.add(nodeUndoX);
							currentPlayer= 1;
							winner = CaroModel.CheckEnd(gridX, gridY);
							if(winner == 2){
								ValueControl.iCntO++;
								ValueControl.isExit = true;
								ValueControl.winLose = "O đã chiến thắng! \n Bạn có muốn tiếp tục?";								
								DialogWinLose();
							}
						}
					}
				}
				}
			}
		});
	}
	
	@Override
	protected void onCreateResources ()  throws IOException {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mBackgroundTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "gfx/bg.png");
		this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(this.mBackgroundTexture);
		this.mBackgroundTexture.load();
		
		this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24);
		this.mFont.load();
		
		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 256, 256);
		this.mBlankTextureRegion = BitmapTextureAtlasTextureRegionFactory.
				createFromAsset(this.mBitmapTextureAtlas, this, 
						"blankIcon.png");
		this.mBnBackTextureRegion = BitmapTextureAtlasTextureRegionFactory.
				createFromAsset(this.mBitmapTextureAtlas, this, 
						"btn_back.png");
		this.mBnNewTextureRegion = BitmapTextureAtlasTextureRegionFactory.
				createFromAsset(this.mBitmapTextureAtlas, this, 
						"btn_new.png");
		this.mBnUndoTextureRegion = BitmapTextureAtlasTextureRegionFactory.
				createFromAsset(this.mBitmapTextureAtlas, this, 
						"btn_undo.png");
		this.mXTextureRegion = BitmapTextureAtlasTextureRegionFactory.
				createFromAsset(this.mBitmapTextureAtlas, this, 
						"xIcon.png");
		this.mOTextureRegion = BitmapTextureAtlasTextureRegionFactory.
				createFromAsset(this.mBitmapTextureAtlas, this, 
						"oIcon.png");
		this.mSettingTextureRegion = BitmapTextureAtlasTextureRegionFactory.
				createFromAsset(this.mBitmapTextureAtlas, this, 
						"btn_setting.png");
		
		try {
			this.mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			this.mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}

	}

	protected Scene onCreateScene() {

		this.mEngine.registerUpdateHandler(new FPSLogger());
		scene = new Scene();
		VBOManager = this.getVertexBufferObjectManager();

		//Background image >>>
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		scene.setBackground(autoParallaxBackground);
		final Sprite spriteBGround = new Sprite(0, 0, this.mBackgroundTextureRegion, VBOManager);
		spriteBGround.setOffsetCenter(0, 0);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0f, spriteBGround));
		//Background image <<<
		
   		CaroBoard.InitCaroBoard();	// khoi tao
   		
		float lineX[] = new float[GRID_WIDTH];
		float lineY[] = new float[GRID_HEIGHT];
		
		final float touchX[] = new float[GRID_WIDTH];
		final float touchY[] = new float[GRID_HEIGHT];
		
		float midTouchX = BOARD_WIDTH / GRID_WIDTH / 2;
		float midTouchY = (BOARD_HEIGHT ) / GRID_HEIGHT / 2;

		float paddingX = midTouchX;
		float paddingY = midTouchY;
		
		for(int i = 0; i < GRID_WIDTH; i++)
		{
			lineX[i] = BOARD_WIDTH / GRID_WIDTH * i;
			touchX[i] = lineX[i] + paddingX;
		}
		for(int i = 0; i < GRID_HEIGHT; i++)
		{
			lineY[i] = (BOARD_HEIGHT ) / GRID_HEIGHT * i;
			touchY[i] = lineY[i] + paddingY;	
		}
		
		// Layout the ButtonSprite
		for(int i = 0; i< GRID_WIDTH; i++){
			for(int j = 0; j< GRID_HEIGHT; j++)
			{
				final ButtonSprite button = new ButtonSprite(touchX[i],
						touchY[j],this.mBlankTextureRegion,
						VBOManager,this);			
				scene.registerTouchArea(button);
				scene.attachChild(button);
				gridSprite[i][j] = button;
			}
		}

		final ButtonSprite buttonback = new ButtonSprite(35,-120,this.mBnBackTextureRegion,this.mOTextureRegion,
				VBOManager,this){
            @Override
            public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
            	if(pAreaTouchEvent.isActionDown()) {
//					System.exit(0);
            		ValueControl.iCntX = 0;
            		ValueControl.iCntO = 0;
        			CaroActivity.board.reset();
        			ValueControl.isExit = false;
        			CaroActivity.clearSprite();
        			CaroActivity.listUndo.clear();
		            finish();
            	}

                return true;
            }
        };
		scene.registerTouchArea(buttonback);
		scene.attachChild(buttonback);
		//Button Back <<<
		
		//Button New >>>
		final ButtonSprite buttonnew = new ButtonSprite(160,-120,this.mBnNewTextureRegion,this.mOTextureRegion,
				VBOManager,this){
            @Override
            public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pAreaTouchEvent.isActionUp()) {

					runOnUiThread(new Runnable(){
						@Override
						public void run() {
							DialogNew();
						}});
				}
	            return true;
            }
        };
	    scene.attachChild(buttonnew);
	    scene.registerTouchArea(buttonnew);
	    //Button New <<<
	    
		//Button Undo >>>
		final ButtonSprite buttonundo = new ButtonSprite(280,-120,this.mBnUndoTextureRegion,this.mOTextureRegion,
				VBOManager,this){
            @Override
            public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
            	if(pAreaTouchEvent.isActionDown()) {
//				showDialog();
				undoList();
            	}

                return true;
            }
        };
	    scene.attachChild(buttonundo);
	    scene.registerTouchArea(buttonundo);
	    //Button Undo <<<
	    
	    //btn_Setting >>>
		final ButtonSprite buttonSetting = new ButtonSprite(390,-120,this.mSettingTextureRegion,this.mOTextureRegion,
				VBOManager,this){
            @Override
            public boolean onAreaTouched(final TouchEvent pAreaTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pAreaTouchEvent.isActionUp()) {

					runOnUiThread(new Runnable(){
						@Override
						public void run() {
							DialogSetting();
						}});
				}
	            return true;
            }
        };
	    scene.attachChild(buttonSetting);
	    scene.registerTouchArea(buttonSetting);
	    //btn_Setting <<<
	    
	    if(ValueControl.isPlayer == 1)
	    {
			//Text nguoi choi >>>
			final Text txtNguoichoi = new Text(120, 450, this.mFont, "Bạn ", new TickerTextOptions(HorizontalAlign.LEFT, 8), this.getVertexBufferObjectManager());
		    scene.attachChild(txtNguoichoi);
		    scene.registerTouchArea(txtNguoichoi);
			//Text nguoi choi <<<
		    
			//Text Máy >>>
			final Text txtMay = new Text(310, 450 , this.mFont, "Máy", new TickerTextOptions(HorizontalAlign.LEFT, 8), this.getVertexBufferObjectManager());
		    scene.attachChild(txtMay);
		    scene.registerTouchArea(txtMay);
			//Text Máy <<<
	    }
	    if(ValueControl.isPlayer == 2)
	    {
			//Text nguoi choi 1 >>>
			final Text txtNguoichoi1 = new Text(118, 450, this.mFont, "NC1", new TickerTextOptions(HorizontalAlign.LEFT, 8), this.getVertexBufferObjectManager());
		    scene.attachChild(txtNguoichoi1);
		    scene.registerTouchArea(txtNguoichoi1);
			//Text nguoi choi <<<
		    
			//Text nguoi choi 2 >>>
			final Text txtNguoichoi2 = new Text(300, 450 , this.mFont, "NC2", new TickerTextOptions(HorizontalAlign.LEFT, 8), this.getVertexBufferObjectManager());
		    scene.attachChild(txtNguoichoi2);
		    scene.registerTouchArea(txtNguoichoi2);
			//Text nguoi choi 2 <<<
	    }
		//Text Tỷ số >>>
		final Text txttyso = new Text(208, 450 , this.mFont,tyso, new TickerTextOptions(HorizontalAlign.LEFT, 8), this.getVertexBufferObjectManager());
	    scene.attachChild(txttyso);
	    scene.registerTouchArea(txttyso);
		//Text Text Tỷ số  <<<
	    
	    //Cập nhật tỷ số
	    scene.registerUpdateHandler(new IUpdateHandler() {
           
            @Override
            public void reset() {
                // TODO Auto-generated method stub
               
            }

            @Override
            public void onUpdate(float pSecondsElapsed) {
                try {
                    //Tạm dừng cập nhật trong 1s
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }               
                //Cập nhật lại những gì bạn cần vẽ lại liên tục
                tyso = String.valueOf(ValueControl.iCntX) + " : " + String.valueOf(ValueControl.iCntO);
                txttyso.setText(String.valueOf(tyso));
            }
        });
	    
		scene.setPosition(30,150);
		scene.setTouchAreaBindingOnActionMoveEnabled(true);

		return scene;

	}

	public void reset() {
		for(int i = 0; i < GRID_WIDTH; i++)
		{
			for(int j = 0; j < GRID_HEIGHT; j++)
			{
				gridSprite[i][j].setEnabled(true);
				gridSprite[i][j].setCurrentTileIndex(0);
			}
		}
		
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if(this.scene.hasChildScene()) {
				this.mMenuScene.back();
			} else {
				this.scene.setChildScene(this.mMenuScene, false, true, true);
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}
	
	
	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
		switch(pMenuItem.getID()) {
			case MENU_RESET:
				this.scene.reset();
				this.scene.clearChildScene();
				this.mMenuScene.resetAnimations();
				return true;
			case MENU_QUIT:
				this.finish();
				return true;
			default:
				return false;
		}
	}
	
	protected void createMenuScene() {
		this.mMenuScene = new MenuScene(this.camera, new AlphaMenuSceneAnimator());

		final SpriteMenuItem resetMenuItem = new SpriteMenuItem(MENU_RESET, this.mMenuResetTextureRegion, this.getVertexBufferObjectManager());
		this.mMenuScene.addMenuItem(resetMenuItem);

		final SpriteMenuItem quitMenuItem = new SpriteMenuItem(MENU_QUIT, this.mMenuQuitTextureRegion, this.getVertexBufferObjectManager());
		this.mMenuScene.addMenuItem(quitMenuItem);

		final SlideMenuSceneAnimator menuSceneAnimator = new SlideMenuSceneAnimator(HorizontalAlign.CENTER, VerticalAlign.CENTER, Direction.UP_LEFT, EaseBounceOut.getInstance());
		menuSceneAnimator.setMenuItemSpacing(10);
		this.mMenuScene.setMenuSceneAnimator(menuSceneAnimator);

		this.mMenuScene.buildAnimations();

		this.mMenuScene.setBackgroundEnabled(false);

		this.mMenuScene.setOnMenuItemClickListener(this);
	}

// Hiển thị Dialog New
    public void DialogNew() {

        try {
            DialogNew dialogNew = new DialogNew(this);
            dialogNew.show();
        } catch (Exception e) {
        }
        
    }
   
 // Hiển thị Dialog Win lose
    public void DialogWinLose() {

        try {
            DialogWin dialogWLose = new DialogWin(this);
            dialogWLose.show();
        } catch (Exception e) {
        }
        
    }

 // Hiển thị Dialog Setting Sound
    public void DialogSetting() {

        try {
            DialogSound dialogNew = new DialogSound(this);
            dialogNew.show();
        } catch (Exception e) {
        }
        
    }

// Hien thi cac hinh anh quan co X va O
    public Scene ShowSpriteXO(int x, int y)
    {
		float lineX[] = new float[GRID_WIDTH];
		float lineY[] = new float[GRID_HEIGHT];
		
		final float touchX[] = new float[GRID_WIDTH];
		final float touchY[] = new float[GRID_HEIGHT];
		
		float midTouchX = BOARD_WIDTH / GRID_WIDTH / 2;
		float midTouchY = (BOARD_HEIGHT ) / GRID_HEIGHT / 2;

		float paddingX = midTouchX;
		float paddingY = midTouchY;
		
		for(int i = 0; i < GRID_WIDTH; i++)
		{
			lineX[i] = BOARD_WIDTH / GRID_WIDTH * i;
			touchX[i] = lineX[i] + paddingX;
		}
		for(int i = 0; i < GRID_HEIGHT; i++)
		{
			lineY[i] = (BOARD_HEIGHT ) / GRID_HEIGHT * i;
			touchY[i] = lineY[i] + paddingY;	
		}
		// Hien thi Sprite quan O khi click vao man hinh
		if(currentPlayer ==2)
		{
	    	final Sprite oPlay = new Sprite(touchX[x],touchY[y],21,21,mOTextureRegion,VBOManager);
	    	scene.attachChild(oPlay);
	    	listSprite.add(oPlay);
		}
		if(currentPlayer ==1)
		{
	    	final Sprite xPlay = new Sprite(touchX[x],touchY[y],21,21,mXTextureRegion,VBOManager);
	    	scene.attachChild(xPlay);
	    	listSprite.add(xPlay);
		}
    	return scene;
    }
// Xoa sprite
    public static void clearSprite() {

        for (int i = 0; i < listSprite.size(); i++) {
                Sprite sprite = listSprite.get(i);
                scene.detachChild(sprite);
        }
        
        listSprite.clear();
    }
    
// Chuc nang undo
    private void undoList()
    {
    	Node n = new Node();
    	Sprite sp;
        if (winner == 0 && listUndo.size() > 2)
        {
        	if(ValueControl.isPlayer == 1)
        	{
        		n = listUndo.get(listUndo.size() - 1);
	            listUndo.remove(listUndo.size() - 1);
	            CaroBoard.BoardArr[n.Row][n.Column] = 0;
	            sp = listSprite.get(listSprite.size()-1);
	            listSprite.remove(listSprite.size()-1);
	            scene.detachChild(sp);
		          if(listUndo.size() > 2)
		          {
		        	  n = listUndo.get(listUndo.size() - 1);
	      	            listUndo.remove(listUndo.size() - 1);
	      	            CaroBoard.BoardArr[n.Row][n.Column] = 0;
	      	            sp = listSprite.get(listSprite.size()-1);
	      	            listSprite.remove(listSprite.size()-1);
	      	            scene.detachChild(sp);
		          }
        	}
        	else if(ValueControl.isPlayer == 2)
        	{
        		n = listUndo.get(listUndo.size() - 1);
	            listUndo.remove(listUndo.size() - 1);
	            CaroBoard.BoardArr[n.Row][n.Column] = 0;
	            sp = listSprite.get(listSprite.size()-1);
	            listSprite.remove(listSprite.size()-1);
	            scene.detachChild(sp);
	            if(currentPlayer == 1)
	            {
	            	currentPlayer = 2;
	            }
	            else currentPlayer = 1;
        	}
        }
    }

    
}


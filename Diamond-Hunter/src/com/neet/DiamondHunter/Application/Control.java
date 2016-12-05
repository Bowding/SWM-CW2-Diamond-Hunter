package com.neet.DiamondHunter.Application;


import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.neet.DiamondHunter.Entity.Item;
import com.neet.DiamondHunter.Manager.Content;
import com.neet.DiamondHunter.TileMap.Tile;
import com.neet.DiamondHunter.TileMap.TileMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ComboBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Control {
	
	@FXML GridPane mapGridPane;
	ImageView tile, axeTile, boatTile;
	Image tileImage, itemImage;
	
	private TileMap tileMap;
	private Image image;
	private int rowNum, colNum;
	private Image boat;
	private Image axe;
	private ArrayList<Item> items;
	private int addingStatus = -1;	//-1:adding nothing; 0:adding boat; 1:adding axe

	//private Graphics2D g = (Graphics2D) image.getGraphics();
	@FXML ComboBox<String> myComboBox;
	@FXML ImageView axeImageView;
	@FXML ImageView boatImageView;
	@FXML Button addAxeButton;
	@FXML Button addBoatButton;
	@FXML Button okButton;
	
	public void initialize() {
		
		items = new ArrayList<Item>();
		tileMap = new TileMap(16);
		
		//load map and tiles
		tileMap.loadTiles("/Tilesets/testtileset.gif");
		tileMap.loadMap("/Maps/testmap.map");
		
		//get row and col numbers
		rowNum = tileMap.getNumRows();
		colNum = tileMap.getNumCols();
		
		//draw map
		int i, j;
		for(i = 0; i < rowNum; i++){
			
			for(j = 0; j < colNum; j++){
				
				tile = new ImageView();
				mapGridPane.add(tile, j, i);
				
				tileImage = getTileImage(i, j);
				
				tile.setImage(tileImage);
				
				tile.setOnMouseClicked(new EventHandler<MouseEvent>(){
					
					@Override
					public void handle(MouseEvent event) {

						if(addingStatus == -1){
							System.out.println("Item not selected!");
						}
						else{
							ImageView grid = (ImageView) event.getSource();
							Integer rowIndex = GridPane.getRowIndex(grid);
							Integer colIndex = GridPane.getColumnIndex(grid);
							
							if(canAdd(rowIndex, colIndex)){
								addItem(rowIndex, colIndex);
								System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
								System.out.println(addingStatus);
							}
							else{
								System.out.printf("Invalid target position [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
							}
						}
						
					}});
			}
		}
		
		//show images in tool bar
		boat = SwingFXUtils.toFXImage(Content.ITEMS[1][0], null);
		axe = SwingFXUtils.toFXImage(Content.ITEMS[1][1], null);
		
		boatImageView.setImage(boat);
		axeImageView.setImage(axe);
	}
	
	private Image getTileImage(int row, int col){
		
		int numTilesAcross = tileMap.getNumTilesAcross();
		int rc = tileMap.getIndex(row, col);
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
	
		//convert awt BufferedImage to javafx Image
		Image image = SwingFXUtils.toFXImage(tileMap.getTileImage(r, c), null);
		return image;
		
	}

	@FXML public void onAddButtonClicked(ActionEvent event) {
		
		if(event.getSource() == addAxeButton){
			addingStatus = 1;	//adding an axe
			//System.out.println(addingStatus);
		}
		else if(event.getSource() == addBoatButton){
			addingStatus = 0;	//adding a boat
			//System.out.println(addingStatus);
		}
	}

	@FXML public void onOkButtonClicked() {
		
		//save file
		//close window
		Stage thisStage = (Stage) okButton.getScene().getWindow();
		thisStage.close();
	}
	
	private void addItem(int row, int col){
		
		Item item;
		int index, previousRow, previousCol;
		
		if(isItemAdded() == -1){	//has not been added
			
			//new an item
			item = new Item(tileMap);
			
			//set item type
			if(addingStatus == 1){
				item.setType(Item.AXE);
				//itemImage = axe;
			}
			else if(addingStatus == 0){
				item.setType(Item.BOAT);
				//itemImage = boat;
			}
			
			//set item position
			item.setTilePosition(row, col);
			
			//add to items arraylist
			items.add(item);
			
			//show item on screen
			showItem(-1, -1, row, col);
			
		}
		else{		//added to the ith position on the items Arraylist
			
			index = isItemAdded();
			item = items.get(index);
			
			previousRow = item.getRow();
			previousCol = item.getCol();
			//update position
			item.setTilePosition(row, col);
			
			showItem(previousRow, previousCol, row, col);
		}
	}

	//if this item is added return the index previously added to, otherwise return -1
	private int isItemAdded(){
		
		if(items.size() == 1){
			
			if(items.get(0).getType() == addingStatus){
				return 0;
			}
		}
		else if(items.size() == 2){
			
			if(items.get(0).getType() == addingStatus){
				return 0;
			}
			else if(items.get(1).getType() == addingStatus){
				return 1;
			}
		}
		
		return -1;
	}
	
	private boolean canAdd(int row, int col){
		//int rc = tileMap.getIndex(row, col);
		if(tileMap.getType(row,col) == Tile.BLOCKED){
			return false;
		}
		
		return true;
		
	}
	
	private void showItem(int preRow, int preCol, int row, int col){

		if(preRow == -1 && preCol ==-1){	//first added
			
			if(addingStatus == 1){	//adding axe
				
				axeTile = new ImageView();
				mapGridPane.add(axeTile, col, row);
				
				axeTile.setImage(axe);
			}
			else if(addingStatus == 0){	//adding boat
				
				boatTile = new ImageView();
				mapGridPane.add(boatTile, col, row);
				
				boatTile.setImage(boat);
			}
		}
		else{								//has been added
			
			if(addingStatus == 1){	//adding axe
				
				GridPane.setColumnIndex(axeTile, col);
				GridPane.setRowIndex(axeTile, row);
			}
			else if(addingStatus == 0){	//adding boat
				
				GridPane.setColumnIndex(boatTile, col);
				GridPane.setRowIndex(boatTile, row);
			}
			
		}

	}
	
}

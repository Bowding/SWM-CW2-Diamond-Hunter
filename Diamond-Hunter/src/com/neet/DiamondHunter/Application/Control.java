/**
 * Controller of map.fxml file
 * Contain event handlers for elements in fxml file, and Control behaviours of the application
 * @author psynw1 (Ning WANG)
 */

package com.neet.DiamondHunter.Application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.neet.DiamondHunter.Entity.Item;
import com.neet.DiamondHunter.Manager.Content;
import com.neet.DiamondHunter.TileMap.Tile;
import com.neet.DiamondHunter.TileMap.TileMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Control {
	
	private ImageView tile, axeTile, boatTile;
	private TileMap tileMap;
	private int rowNum, colNum;
	private Image axe, boat, tileImage;
	private ArrayList<Item> items;
	private int addingStatus = -1;	//-1:adding nothing; 0:adding boat; 1:adding axe

	@FXML GridPane mapGridPane;
	@FXML ComboBox<String> myComboBox;
	@FXML ImageView axeImageView;
	@FXML ImageView boatImageView;
	@FXML Button addAxeButton;
	@FXML Button addBoatButton;
	@FXML Button okButton;
	
	/**
	 * Initialisation for Control class
	 */
	public void initialize() {
		
		items = new ArrayList<Item>();
		tileMap = new TileMap(16);
		
		//load map and tiles
		tileMap.loadTiles("/Tilesets/testtileset.gif");
		tileMap.loadMap("/Maps/testmap.map");
		
		//get row and col numbers
		rowNum = tileMap.getNumRows();
		colNum = tileMap.getNumCols();
		
		//draw game map
		int i, j;
		for(i = 0; i < rowNum; i++){
			
			for(j = 0; j < colNum; j++){
				
				tile = new ImageView();
				mapGridPane.add(tile, j, i);
			
				tileImage = getTileImage(i, j);
				tile.setImage(tileImage);
				
				//assign new mouse clicked event handler to every tile
				tile.setOnMouseClicked(new EventHandler<MouseEvent>(){
					
					@Override
					public void handle(MouseEvent event) {

						if(addingStatus == -1){
							
							//show warning alert if no item type selected
					        event.consume();

					        Alert alert = new Alert(AlertType.WARNING);
					        alert.setTitle("Warning");
					        alert.setHeaderText("No item selected");
					        alert.setContentText("Please select one item to add : )");

					        alert.showAndWait();
					       
						}
						else{
							
							//get selected position
							ImageView grid = (ImageView) event.getSource();
							Integer rowIndex = GridPane.getRowIndex(grid);
							Integer colIndex = GridPane.getColumnIndex(grid);
							
							//position validation
							if(canAdd(rowIndex, colIndex)){
								
								//add item if the target position is valid
								addItem(rowIndex, colIndex);
								
								//System.out.printf("Mouse entered cell [%d, %d]%n", rowIndex.intValue(), colIndex.intValue());
								//System.out.println(addingStatus);
							}
							else{
								
								//show warning alert if try to add to an invalid tile
						        event.consume();

						        Alert alert = new Alert(AlertType.WARNING);
						        alert.setTitle("Warning");
						        alert.setHeaderText("Invalid target position");
						        alert.setContentText("Please add to another position : )");

						        alert.showAndWait();
						        
								//System.out.printf("Invalid target position [%d, %d]%n", rowIndex.intValue(), colIndex.intValue());
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
	
	
	/**
	 * Return tile image by coordinates
	 * @param row
	 * @param col
	 * @return	tile image
	 */
	private Image getTileImage(int row, int col){
		
		int numTilesAcross = tileMap.getNumTilesAcross();
		int rc = tileMap.getIndex(row, col);
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
	
		//convert awt BufferedImage to javafx Image
		Image image = SwingFXUtils.toFXImage(tileMap.getTileImage(r, c), null);
		return image;
		
	}

	/**
	 * Change adding status when addAxeButton or addBoatButton clicked
	 * @param event
	 */
	@FXML public void onAddButtonClicked(ActionEvent event) {
		
		if(event.getSource() == addAxeButton){
			
			addingStatus = 1;	//adding an axe
		}
		else if(event.getSource() == addBoatButton){
			
			addingStatus = 0;	//adding a boat
		}
	}

	/**
	 * Write positions of axe and boat to file and close window when OK button clicked
	 * @param event
	 */
	@FXML public void onOkButtonClicked(ActionEvent event) {
		
		//check whether both items are added
		if(items.size() != 2){
			
			//show warning alert if items insufficient
	        event.consume();
	        
	        Alert alert = new Alert(AlertType.WARNING);
	        alert.setTitle("Warning");
	        alert.setHeaderText("Please add both items : )");
	        
	        alert.showAndWait();
			
			return;
		}
		
		//get item from arraylist
		Item item0 = items.get(0);
		Item item1 = items.get(1);
		
		//get item type and position
		int iType0 = item0.getType();
		int iRow0 = item0.getx();
		int iCol0 = item0.gety();
		
		int iType1 = item1.getType();
		int iRow1 = item1.getx();
		int iCol1 = item1.gety();
		
		String fileInString = String.format("%d\n%d\n%d\n%d\n%d\n%d\n", iType0, iRow0, iCol0, iType1, iRow1, iCol1);

		//write to file
		String filename = "../Resources/Maps/itemPosition.data";
        BufferedWriter bw;
        try {
            File file = new File(filename);
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(fileInString);
            bw.close();
        } catch ( IOException e ) {
        	
        	//if exception caught, using alert to give error message
	        event.consume();

	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Something went wrong...");
	        alert.setHeaderText("Unable to save the settings");

	        alert.showAndWait();
        	
        }
		
		//close window
		Stage thisStage = (Stage) okButton.getScene().getWindow();
		thisStage.close();
	}
	
	/**
	 * Add item to assigned coordinates and control display of added item
	 * @param row
	 * @param col
	 */
	private void addItem(int row, int col){
		
		Item item;
		int index, previousRow, previousCol;
		
		if(isItemAdded() == -1){	//has not been added
			
			//new an item
			item = new Item(tileMap);
			
			//set item type
			if(addingStatus == 1){
				item.setType(Item.AXE);
			}
			else if(addingStatus == 0){
				item.setType(Item.BOAT);
			}
			
			//set item position
			item.setPosition(row, col);
			
			//add to items arraylist
			items.add(item);
			
			//display to screen
			showItem(-1, -1, row, col);
			
		}
		else{						//has been added
			
			//get previously added axe/boat index in arraylist
			index = isItemAdded();
			item = items.get(index);
			
			//copy previous coordinates
			previousRow = item.getx();
			previousCol = item.gety();
			
			//update position
			item.setPosition(row, col);
			
			//display to screen
			showItem(previousRow, previousCol, row, col);
		}
	}

	/**
	 * Check whether this type of item has been added before
	 * if so return the index previously added to, otherwise return -1
	 * @return -1 or index of previously added item 
	 */
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
	
	/**
	 * Display item image on screen or update the image position if added before
	 * @param preRow
	 * @param preCol
	 * @param row
	 * @param col
	 */
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

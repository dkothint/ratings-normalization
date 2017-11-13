package org.prasad.ratingsnormalization.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.prasad.ratingsnormalization.io.Launcher;
import org.prasad.ratingsnormalization.util.PropsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * This class is responsible for loading the raw json data into the DB
 * @author 1
 *
 */
public class DataLoader {

	public static Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

	/*
	 * Creates the necessary tables and loads the data into them
	 */
	public void loadData() {
		
		DBConnectionManager.getInstance().createReviewsTable();
		DBConnectionManager.getInstance().createBusinessesTable();
		
		Gson gson = new Gson();
		try {
			PropertiesConfiguration props = PropsUtil.getInputConfiguration();
			BufferedReader br = new BufferedReader(
					new FileReader(props.getString("reviewsFilePath")));
			String line;
			DBConnectionManager connManager = DBConnectionManager.getInstance();
			Connection conn = connManager.getConnection();
			long begin = System.currentTimeMillis();
			while ((line = br.readLine()) != null) {
				ReviewsTuple review = gson.fromJson(line, ReviewsTuple.class);
				connManager.insertIntoReviewsTable(review, conn);
			}
			long end = System.currentTimeMillis();
			br.close();
			
			LOGGER.debug("Time taken for loading reviews: "+(end-begin));
			
			br = new BufferedReader(
					new FileReader(props.getString("businessFilePath")));
			begin = System.currentTimeMillis();
			while ((line = br.readLine()) != null) {
				BusinessTuple business = gson.fromJson(line, BusinessTuple.class);
				connManager.insertIntoBusinessTable(business, conn);
			}
			end = System.currentTimeMillis();
			
			
			connManager.closeConnection(conn);
			LOGGER.debug("Time taken for loading businesses: "+(end-begin));
			
			br.close();
			
		} catch (FileNotFoundException e) {
			LOGGER.error("FileNotFound exception thrown for Input data file!", e);
		} catch (IOException e) {
			LOGGER.error("IOException exception thrown for Input data file!", e);
		} catch (ConfigurationException e) {
			LOGGER.error("Input configuration file retrieval error!", e);
		}
	}
}

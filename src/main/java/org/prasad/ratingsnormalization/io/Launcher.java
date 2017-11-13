package org.prasad.ratingsnormalization.io;

import org.prasad.ratingsnormalization.core.NormalizationEvaluator;
import org.prasad.ratingsnormalization.core.ReviewsAnalyzer;
import org.prasad.ratingsnormalization.db.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the entry point into the application.
 * @author DurgaPrasad
 *
 */
public class Launcher {

	public static Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

	public static void main(String[] args) {
		Launcher launcher = new Launcher();
		launcher.process();
	}

	private void process() {
		DataLoader dataLoader = new DataLoader();
		dataLoader.loadData();
		LOGGER.info("Data loading completed!");

		ReviewsAnalyzer reviewsAnalyzer = new ReviewsAnalyzer();
		reviewsAnalyzer.analyzeReviews();
		LOGGER.info("Reviews analysis completed!");
		
		NormalizationEvaluator normEvaluator = new NormalizationEvaluator();
		normEvaluator.evaluateNormalization();
		LOGGER.info("Normalization Evaluation completed!");
	}
	
}

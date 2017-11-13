# ratings-normalization
An interesting application which mines the Yelp dataset to identify and normalize the average ratings of businesses affected by ratings from 'stingy' reviewers.

Who is a 'stingy' reviewer? 
Well, he/she is someone who is stingy with the ratings. Speaking in technical terms, a reviewer is considered 'stingy' if the following 2 conditions are met:
1. He/she has given enough number of reviews, say 10 or more.
2. All the ratings given by the user are strictly lesser than the average ratings of the corresponding business.

The problem...
In this internet generation where star ratings drive success of businesses with online presence, each decimal point rating matters.   

Ratings are a big influencing factor in the growth rate of businnesses. Better the rating, faster the business grows and a logical extension to this is the counter statement that poorer the rating, slower the growth rate. So, it is extremely important that new businneses keep their ratings high to make sure they grow faster. 
Also, prior ratings tend to influence significant chunk of users while giving their ratings as well - although a user loved a restaurant, if its prior ratings were poor, it is possible that he/she rates it a 4 on 5 instead of 4.5 or 5 on 5, which could have been the case if the prior ratings were top notch. 

It is therefore extremely important for new businesses to have a great start with their ratings and this is where 'stingy' reviewers can have a huge negative influence. What if Restaurant 'A' had a significant chunk of initial set of ratings coming from stingy reviewers and Restaurant 'B' had most ratings from non-stingy reviewers. Clearly, we know what would be the impact of this. And that is the reason, ratings normalization could prove to be useful. 

The Goal...
This application attempts to normalize the average ratings of businesses in their early days by identifying and fixing the ratings given by 'stingy' reviewers.

Technologies used:
This is a simple stand-alone JAVA application meant to be run periodically using job scheduling tools like Cron. 
Maven is used for project build management. 
Gson is used for working with JSON data,mainly for its ease of use. 
Postgres is the database used because of its flexibility and performance.

A simple web-app with RESTful web services is also developed to access the results of this application.
Please refer to this repo: 




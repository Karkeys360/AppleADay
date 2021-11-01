# AppleADay - Submission for Dubhacks 21 hackathon

[Check out our product demo video here!](https://youtu.be/JXuO2fxLGBE)

## Inspiration
As we transition to in-person college, we became more conscious about healthy eating on campus. As such, we wanted to develop an application to track our diets more holistically. In addition, we wanted the application to monitor each user's personalized eating habits and present them with ways to be more healthy. 

## What it does
The app allows users to scan barcodes to enter the foods they consumed throughout the day. The app also allows us to record any bodily reactions following intake, such as bloating or flatulence. In-app, these bodily reactions are referred to as "events". The app performs a Bayesian analysis of these events to correlate intake with bodily sensitivities.
 
The app then presents a dashboard of your nutrient consumption and macros (fats, carbohydrates, proteins) consumption over time. You may also view a chart of your body's level of sensitivity to different foods. 

## How we built it
We built an Android application. The user takes a picture of the barcode on a food item they consume, and the app scans the barcode using Google MLKit. The app then queries the Open Food Facts API to get nutrient information on foods given this barcode. Users may enter eating-related events, like bloating or flatulence, in the app as well. The app performs analytics on the correlation between various foods and these bodily reactions using Bayes Theorem. It outputs the likelihood these reactions were caused by the foods they ate.

## Challenges we ran into
We found programmatically populating data in Android apps challenging. Developing an appealing UI was also a challenge, but we learned a lot in the process.

## Accomplishments that we're proud of
We're proud of our statistical analysis between food and bodily reactions. Over the course of the hackathon, we felt that we did a good job of maintaining a quick development pace while taking time to review each other's code. We were proud to learn new technologies and implement them quickly and effectively.

## What we learned
We learned how to implement an algorithm to scan a barcode. We also learned to adapt quickly to design changes or new issues. We learned to manage expectations about what is possible to accomplish in the duration of a hackathon.

## What's next for Apple A Day
We would like to incorporate more advanced analytics as well as other health data, such as exercise.

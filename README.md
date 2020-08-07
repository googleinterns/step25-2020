# Google Classroom AutoGrader | step25-2020

This repo contains the source code for the STEP capstone project of Ben, Mahima, and Sampson.

One big change we’re seeing right now (2020) is schools all over the country being moved online this Fall due to the COVID-19 Pandemic. Teachers will have to shift to giving instruction online. However, teachers are already overworked, oftentimes working 51-53 hours weeks on average, with 25-35% of their time being spent grading and planning. We can even expect this amount to increase as teachers have to grade online submissions. Not only may this make grading for teachers more time consuming, but difficult to use as many teachers still use worksheets as assignments in primary and secondary education. 
 
Currently, Google Classroom has integrated Google Drive and Forms, allowing teachers to automatically score multiple choice questions. However, there each short response question still needs to be graded by a teacher. Autograding is also not compatible with student worksheets at the moment. The scope of this project would be to gather student submissions from an instructor’s Google Classroom on individual assignments. The product would then allow the teacher to indicate the area on a worksheet where student responses are placed.
The feature will then parse all submissions for the assignment question and sort similar responses into buckets. This would allow the teacher to grade all similar responses within a bucket at once, greatly reducing the amount of time teachers spend on grading, by assigning a point value for all similar submissions rather than assign grade values for each individual submission.
 
The application will allow the teacher to download all student grades, manually re-sort buckets where the Google Vision API makes a mistake in classifying submissions, as well as export grades back to Google Classroom. 

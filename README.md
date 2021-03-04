# FLIX - Android Movie App 
#### Taake-home project I completed in a week as part of a job interview. The application lays out tiles for movie details fetched from an [TMDb](https://www.themoviedb.org/?language=en-US) using an API key provided by the interviewing company. All calls to API and loading images are asynchronous. Movies are then stored offline in a NoSQL database and retrieved from cache when possible.

##### Features:
- poster text scrolls when title is too long
- cardview/poster image do not resize on keyboard popup
- keyboard disappears after searchImageButton or keyboard enter button is clicked
- app alerts user when search doesnt yield any results
- user can filter by genre and search by title within filter
- user can store favorite movies and access favorites list from main activity
- GET request to API only happens if offline cache is empty, otherwise search/filter/data manipulation all done offline

##### Libraries/tools being used: 
- [RecyclerView](https://developer.android.com/reference/android/support/v7/widget/RecyclerView): to view the movies as a list
- [Retrofit](https://github.com/square/retrofit): to retrieve api results from db
- [CardView](https://developer.android.com/reference/android/support/v7/widget/CardView): to display movie poster and name as a tiles
- [Paperdb](https://github.com/pilgr/Paper): NoSQL db to store movie info offline in app
- [Picasso](https://github.com/square/picasso): to place images in imageview
- [Nice Spinner](https://github.com/arcadefire/nice-spinner): to display genres
- used maven{ [jitpack](https://jitpack.io/) } to successfully some of these ^

##### Note on scalability: 
The offline db that I used is not optimal for scaling up since it pulls out all the data at once rather than being able to pick and choose. I chose it in this project's context because it was the easiest to  learn and implement in the given time.

##### Note on testing: 
I've reviewed a couple videos that talked about unit and integration/UI tests. I tried to implement some manually and some using Espresso. I got into a few bugs that made it a real pain. I think I'm going to need more time  figuring out how to optimally create tests for Android apps.

Lab B.1 | Programming & Integrating Native code.

Aim

During this lab, you are asked to create the frontend for a N-back logic game. The N-back game is a
game that tests the working memory of the test subjects. Subjects are shown stimuli (audio, visual,
text, etc.) and are asked to perform an action when the stimulus is the same as the stimulus in the
position n-back (so X-1-2-X would require an action for n-back with n=3).
The developers of this game want the game to work on the web, as well as on iOS and Android.
Therefore, they have developed a model in C that is to be used to generate a string of N-back stimuli.
Your job is to handle this string and use it to create the N-back game.
With this assignment you will learn about graphics, touch events, animations, and integrating native
code into your own application.


General

This assignment is solved Individually. You will have to solve this assignment before you are allowed
to continue the course. As it would be in the real world; you are not required to create all parts of
this application yourself, but you will be provided with a project backbone that is not functional yet.
The working of this backbone will be clarified during the second lecture.

To ensure the quality of your work, please adhere to the following guidelines:

- The solution should be separated in a set of appropriate classes.
- Names of classes, methods and fields should describe their usage.
- Structure and document your code effectively. It is recommended to follow the MVVM (Model-View-ViewModel) architecture. While MVC (Model-View-Controller) is also acceptable, keep in mind that applications with “god” classes will be considered insufficient.
- NB! It is mandatory to submit your code at least a day prior to your presentation.


N-back background

The n-back task is a continuous performance task that is commonly used as an assessment in psychology and cognitive neuroscience to measure working memory capacity. It is also used as training to enhance memory capacity.
The person to be assessed, or in training, is presented with a sequence of stimuli, that for example can be visual (color or position of an object displayed) or auditory (a letter spoken).
The task consists of indicating when the current stimulus matches the one from n steps earlier in the sequence, hence the name n-back.
The dual-task n-back task is a variation where two independent sequences are presented simultaneously, typically one auditory and one visual.
You can try dual-n-back at https://brainscale.net/dual-n-back/training. You can change settings, like n-back or dual n-back and the value of n, the upper right corner of the view.


Basic requirements (1 point)

Create a simple yet effective application that allows the user to perform a single-n-back test,
where the user can either choose visual or auditory stimuli. The customers want the application to have at least two views; 
one home screen in which the user will see the high score and on which the user can choose between different combinations of visual or auditory stimuli, 
and one view for the actual game. More detailed requirements for this version of the application are found in “Detailed requirements”.


Higher level requirements (2 points)

For a higher grade the customers would also like users to be able to perform a dual-n-back test. During this test,
users are given both visual and auditory stimuli simultaneously, and need to keep track of both.
Furthermore, you should implement at least one of the following points:

- Have a settings screen in which the user can choose the number of events in a round, the time between the events, the “n” for the n-back, the size of the grid for visual stimuli (e.g., 5x5 instead of 3x3), and the number of spoken letters for the auditory stimuli). (These settings need to be persistent)
- Users must be able to save their score under a name and see all scores in a different view. You need to provide to sort the scores by name, score, and date.
- The application must work both in landscape and portrait mode, as well as on smaller screen sizes and bigger screen sizes (e.g., tablet).


Detailed requirements

The below are mandatory to pass the assignments.
- Show the visual stimuli in a 3x3 grid.
- Audio stimuli are provided as spoken letters (a limited set of letters).
- Users controls to start a new round, and to input an n-back match.
- Information on the current settings on the home screen (visual/audio, value of n, time between events, and number of events in a round)
- Information on the state in an ongoing round, for example the current event number and the number of correct responses.
- Non-offensive feedback when the user makes an erroneous guess (e.g., animate the size or color of a button)


Hints

Android

- For validating various display dimensions, consider using tablet or phone emulators.
- Consider saving your results in a room database and use that database to provide you with sorted data.
- Consider using Datastore<Preferences> for saving smaller/easier values like settings persistently.

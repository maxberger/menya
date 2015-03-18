# Meeting Summaries #

## Ideas from meeting on 05. Aug. 2009 ##

We just met in Innsbruck to decide the next steps. Future versions of MENYA should include following features:
  * zoom feature
  * increase / decrease size of a page dynamically by buttons at the border of a page
  * network layer to draw in a meeting
  * fix write of layers, currently it is quite buggy and stores a second (empty) layer
  * introduce pens:
    * pens have a color
    * pens can have a thickness/pressure graph (a editable curve would be very cool for wacom pens)
    * pens can be assigned to a physical device
  * new structure to store documents (maybe implement both possibilities?)
    * idea 1:
      * we have "books"
      * books contain "chapters"
      * each "chapter" contains "pages"
      * this would be directly applicable and storable as directory structure
    * idea 2:
      * a tagging system
      * a bit more complicated
  * smooth & optimize curves while drawing / writing notes
  * optimize storage of pdf (why is a pdf with 5 lines 200K big?)
  * background
  * text tools (to write text)
  * possibilities to detect standard geometric objects ( triangle, arrows, boxes, circles, ellipse )
    * cool idea: general neural network that can be trained to detect arbitrary things
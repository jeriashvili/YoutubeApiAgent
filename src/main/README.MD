YoutubeAgent 

frontend - ის გასაშვებად საჭირო გარემო  : node.js ვერსია : 16.15.0 , npm -ის ვერსია 8.5.5 , angular ის ვერსია 14.0.1
backend -ში გამოყენებული ძირითადი ტექნოლოგიები  : 
java,
springboot,
h2 in memory db (application property ფაილში მითითებულია ფაილის მისამართI რადგან ყოველი გაშვების დროს არ წაიშალოს ინფორმაცია და ფაილიდან აიღოს) ,
Spring Data JPA
Spring Security ( with Basic Authentication)

აპლიკაციის გასაშვებად უნდა დაისტარტოს ორივე აპლიკაცია სინქრონულად ( YoutubeAgent და YoutubeAgent-front) 
და http://localhost:4200/  მისამართზე გაიხსნება ავტორიზაციის და რეგისტრაციის გვერდი.
მომხმარებლის რეგისტრაციისას country ის ველში უნდა ჩაიწეროს Alpha-2 code ის პროტოკოლით გათვალისწინებული 2 სიმბოლოიანი კოდი (მაგ: GE,DE და ასე შემდეგ)





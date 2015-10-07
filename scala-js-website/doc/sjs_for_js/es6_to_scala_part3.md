---
layout: doc
title: "From ES6 to Scala: Advanced"
---

Scala is a feature rich language that is easy to learn but takes time to master. Depending on your programming
background, typically you start by writing Scala as you would've written the language you know best (JavaScript, Java or
C# for example) and gradually learn more and more idiomatic Scala paradigms to use. In this section we cover some of the
more useful design patterns and features, to get you started quickly.

## Pattern matching

In the Basics part we already saw simple examples of _pattern matching_ as a replacement for JavaScript switch
statement. However, it can be used for much more, for example checking the type of input.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
function printType(o) {
  switch (typeof o) {
    case "string":
      console.log(`It's a string: ${o}`); 
      break;
    case "number":
      console.log(`It's a number: ${o}`); 
      break;
    case "boolean":
      console.log(`It's a boolean: ${o}`); 
      break;
    default:
      console.log(`It's something else`);
  }
}
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
def printType(o: Any) {
  o match {
    case s: String =>
      println(s"It's a string: $s")
    case i: Int =>
      println(s"It's an int: $i")
    case b: Boolean =>
      println(s"It's a boolean: $b")
    case _ =>
      println("It's something else")
}
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

Pattern matching uses something called _partial functions_ which means it can be used in place of regular functions, for
example in a call to `filter` or `map`. You can also add a _guard clause_ in the form of an `if`, to limit the match. If
you need to match to a variable, use backticks to indicate that.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
function parse(str, magicKey) {
  let res = [];
  for(let c of str) {
    if (c === magicKey)
      res.push("magic");
    else if (c.match(/\d/))
      res.push("digit");
    else if (c.match(/\w/))
      res.push("letter");
    else if (c.match(/\s/))
      res.push(" ");
    else 
      res.push("char");
  }
  return res;
}
const r = parse("JB/007", '/');
// [letter, letter, magic, digit, digit, digit]
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
def parse(str: String, magicKey: Char)= {
  str.map {
    case c if c == magicKey =>
      "magic"
    case c if c.isDigit =>
      "digit"
    case c if c.isLetter =>
      "letter"
    case c if c.isWhitespace =>
      " "
    case c =>
      "char"
  }
}
val r = parse("JB/007", '/')
// Seq(letter, letter, magic, digit, digit, digit)
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

#### Destructuring

Where pattern matching really shines is at destructuring. This means matching to a more complex pattern and extracting
values inside that structure. ES6 also supports destructuring (yay!) in assignments and function parameters, but not
in matching.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
const person = {first: "James", last: "Bond", 
  age: 42};
const {first, last, age: years} = person;
// first = "James", last = "Bond", years = 42
const seq = [1, 2, 3, 4, 5];
const [a, b, , ...c] = seq;
// a = 1, b = 2, c = [4, 5]

const seq2 = [a, b].concat(c); // [1, 2, 4, 5]
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
case class Person(first: String, last: String, 
  age: Int)
val person = Person("James", "Bond", 42)
val Person(first, last, years) = person
// first = "James", last = "Bond", years = 42
val seq = Seq(1, 2, 3, 4, 5)
val Seq(a, b, _, c @ _*) = seq 
// a = 1, b = 2, c = Seq(4, 5)

val seq2 = Seq(a, b) ++ c // Seq(1, 2, 4, 5)
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

In Scala the destructuring and rebuilding have nice symmetry making it easy to remember how to do it. Use `_` to skip
values in destructuring.

In pattern matching the use of destructuring results in clean, simple and understandable code.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
function ageSum(persons, family) {
  return persons.filter(p => p.last === family)
    .reduce((a, p) => a + p.age, 0);
}
const persons = [
  {first: "James", last: "Bond", age: 42},
  {first: "Hillary", last: "Bond", age: 35},
  {first: "James", last: "Smith", age: 55}
];

ageSum(persons, "Bond") == 77;
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
def ageSum(persons: Seq[Person], 
           family: String): Int = {
  persons.collect {
    case Person(_, last, age) if last == family => 
      age
  }.sum
}
val persons = Seq(
  Person("James", "Bond", 42),
  Person("Hillary", "Bond", 35),
  Person("James", "Smith", 55)
)

ageSum(persons, "Bond") == 77
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

We could've implemented the Scala function using a `filter` and `foldLeft`, but it is more understandable using
`collect` and pattern matching. It would be read as "Collect every person with a last name equaling `family` and extract
the age of those persons. Then sum up the ages."

Another good use case for pattern matching is regular expressions (also in ES6!). Let's extract a date in different
formats.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
function convertToDate(d) {
  const YMD = /(\d{4})-(\d{1,2})-(\d{1,2})/
  const MDY = /(\d{1,2})\/(\d{1,2})\/(\d{4})/
  const DMY = /(\d{1,2})\.(\d{1,2})\.(\d{4})/
  {
    const [, year, month, day] = YMD.exec(d) || [];
    if (year !== undefined)
      return {year: year, month: month, day: day};
    else {
      const [, month, day, year] = 
        MDY.exec(d) || [];
      if (year !== undefined)
        return {year: year, month: month, day: day};
      else {
        const [, day, month, year] = 
          DMY.exec(d) || [];
        if (year !== undefined)
          return {year: year, month: month, 
            day: day};
      }
    }
  }
  throw "Invalid date!";
}
convertToDate("2015-10-9"); // {year:2015,month:10,day:9}
convertToDate("10/9/2015"); // {year:2015,month:10,day:9}
convertToDate("9.10.2015"); // {year:2015,month:10,day:9}
convertToDate("10 Nov 2015") // exception
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
case class Date(year: Int, month: Int, day: Int)

def convertToDate(d: String): Date = {
  val YMD = """(\d{4})-(\d{1,2})-(\d{1,2})""".r
  val MDY = """(\d{1,2})/(\d{1,2})/(\d{4})""".r
  val DMY = """(\d{1,2})\.(\d{1,2})\.(\d{4})""".r
  d match {
    case YMD(year, month, day) => 
      Date(year.toInt, month.toInt, day.toInt)
    case MDY(month, day, year) =>
      Date(year.toInt, month.toInt, day.toInt)
    case DMY(day, month, year) =>
      Date(year.toInt, month.toInt, day.toInt)
    case _ => 
      throw new Exception("Invalid date!")
  }
}

convertToDate("2015-10-9") // = Date(2015,10,9)
convertToDate("10/9/2015") // = Date(2015,10,9)
convertToDate("9.10.2015") // = Date(2015,10,9)
convertToDate("10 Nov 2015") // exception
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

Here we use triple-quoted strings that allow us to write regex without escaping special characters. The string is
converted into a `Regex` object with the `.r` method. Because regex extracts strings, we need to convert matched groups
to integers ourselves.

## Implicits

Being type safe is great in Scala, but sometimes the type system can be a bit prohibitive when you want to do something
else, like add methods to existing classes. To allow you to do this in a type safe manner, Scala provides _implicits_.
You can think of implicits as something that's available in the scope when you need it, and the compiler can
automatically provide it. For example we can provide a function to automatically convert a JavaScript `Date` into a
Scala/Java `Date`

{% columns %}
{% column 9 Scala %}
{% highlight scala %}
import scalajs.js

implicit def convertFromJSDate(d: js.Date): java.util.Date = {
  new java.util.Date(d.getMilliseconds())
}

implicit def convertToJSDate(d: java.util.Date): js.Date = {
  new js.Date(d.getTime)
}

case class Person(name: String, joined: js.Date)

val p = Person("James Bond", new java.util.Date)
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

When these implicit conversion functions are in lexical scope, you can use JS and Scala dates interchangeably. Outside
the scope they are not visible and you must use correct types or provide conversion yourself.

#### Implicit conversions for "monkey patching" 

Monkey patching -term became famous among Ruby developers and it has been adopted into JavaScript to describe
a way of extending existing classes with new methods. It has several pitfalls in dynamic languages and is generally
not a recommended practice. Especially dangerous is to patch JavaScript's host objects like `String` or `DOM.Node`. This
technique is, however, commonly used to provide support for new JavaScript functionality missing from older JS engines.
The practice is known as _polyfilling_ or _shimming_.

In Scala providing extension methods via implicits is _perfectly safe_ and even a _recommended_ practice. Scala
standard library does it all the time. For example did you notice the `.r` or `.toInt` functions that were used on
strings in the regex example? Both are extension methods coming from implicit classes.

Let's use the `convertToDate` we defined before and add a `toDate` extension method to `String` by defining an _implicit
class_.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
String.prototype.toDate = function() {
  return convertToDate(this);
}
"2015-10-09".toDate; // = Date(2015,10,9)
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
implicit class StrToDate(val s: String) 
  extends AnyVal {
  def toDate = convertToDate(s)
}
"2015-10-09".toDate // = Date(2015,10,9)
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

Note that the JavaScript version modifies the global `String` class (dangerous!), whereas the Scala version only
introduces a conversion from `String` to a custom `StrToDate` class providing an additional method. Implicit classes are
_safe_ because they are lexically scoped, meaning the `StrToDate` is not available in other parts of the program unless
explicitly imported. The `toDate` method is not added to the `String` class in any way, instead the compiler generates
appropriate code to call it when required. Basically `"2010-10-09".toDate` is converted into `new
StrToDate("2010-10-09").toDate` which is then inlined/optimized (due to the use of Value Class) to
`convertToDate("2010-10-09")` at the call site.

Scala IDEs are also smart enough to know what implicit extension methods are in scope and will show them to you next
to the other methods.

![Extension method in IDE]({{site.baseurl}}/assets/img/implicitIDE.png)

## Futures

Writing asynchronous JavaScript code used to be painful due to the number of callbacks required to handle chained
asynchronous calls. This is affectionately known as _callback hell_. Then came the various Promise libraries that
alleviated this issue a lot, but were not fully compatible with each other. ES6 standardizes the `Promise` interface so
that all implementations (ES6's own included) can happily coexist.


{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

---
layout: doc
title: "From ES6 to Scala: Collections"
---

In JavaScript there are basically two kinds of collections you have used to store your data: the `Array` for sequential
data and `Object` (aka dictionary or hash map) for storing key-value pairs. Furthermore both of these are mutable by
default, so if you pass them to a function, that function might go and modify them without your knowledge.

ES6 extends your options with four new collection types `Map`, `Set`, `WeakMap` and `WeakSet`. Of these the `WeakMap`
and `WeakSet` are for special purposes only, so in your application you would typically use only `Map` and `Set`.

## Scala collection hierarchy

Unlike JavaScript, the Scala standard library has a huge variety of different collection types to choose from.
Furthermore the collections are organized in a type hierarchy, meaning they share a lot of common functionality and
interfaces. The high-level hierarchy for the abstract base classes and traits is shown in the image below.

![Scala collection hierarchy](http://docs.scala-lang.org/resources/images/collections.png)

Scala provides _immutable_ and _mutable_ implementations for all these collection types.

<table class="table table-bordered">
<tr><th colspan="2"><h6>Common <i>immutable</i> collections</h6></th></tr>
<tr><td>Seq</td><td>List, Vector, Stream, Range</td></tr>
<tr><td>Map</td><td>HashMap, TreeMap</td></tr>
<tr><td>Set</td><td>HashSet, TreeSet</td></tr>
<tr><th colspan="2"><h6>Common <i>mutable</i> collections</h6></th></tr>
<tr><td>Seq</td><td>Buffer, LinkedList, Queue</td></tr>
<tr><td>Map</td><td>HashMap, LinkedHashMap</td></tr>
<tr><td>Set</td><td>HashSet</td></tr>
</table>

## Comparing to JavaScript

Let's start with familiar things and see how Scala collections compare with the JavaScript `Array` and `Object` (or
`Map`). The closest match for `Array` would be the mutable `Buffer` since arrays in Scala cannot change size after
initialization. For `Object` (or `Map`) the best match is the mutable `HashMap`.

A simple example of array manipulation.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
const a = ["Fox", "jumped", "over"];
a.push("me"); // Fox jumped over me
a.unshift("Red"); // Red Fox jumped over me
const fox = a[1];
a[a.length - 1] = "you"; // Red Fox jumped over you
console.log(a.join(" ")); 
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
import scala.collection.mutable
val a = mutable.Buffer("Fox", "jumped", "over")
a.append("me") // Fox jumped over me
a.prepend("Red") // Red Fox jumped over me
val fox = a(1)
a(a.size - 1) = "you" // Red Fox jumped over you
println(a.mkString(" "))
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}


Working with a hash map (or Object).

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
const p = {first: "James", last: "Bond"};
p["profession"] = "Spy";
const name = `${p.first} ${p.last}`
{% endhighlight %}
{% endcolumn %}
        
{% column 6 Scala %}
{% highlight scala %}
import scala.collection.mutable
val p = mutable.HashMap("first" -> "James", 
  "last" -> "Bond")
p("profession") = "Spy"
val name = s"${p("first")} ${p("last")}"
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

Even though you can use Scala collections like you would use arrays and objects in JavaScript, you really shouldn't,
because you are missing a lot of great functionality.

## Common collections `Seq`, `Map`, `Set` and `Tuple`

For 99% of the time you will be working with those four common collection types in your code. You will instantiate
implementation collections like `Vector` or `HashMap`, but in your code you don't really care what the implementation is,
as long as it behaves like a `Seq` or a `Map`.


#### Tuple

You may have noticed that `Tuple` is not shown in the collection hierarchy above, because it's a very specific
collection type of its own. Scala tuple combines a fixed number of items together so that they can be passed around as a
whole. A tuple is immutable and can hold different types, so it's quite close to an anonymous case class in that sense.
Tuples are used in situations where you need to group items together, like key and value in a map, or to return multiple
values. In JavaScript you can use a fixed size array to represent a tuple.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
const t = ["James", "Bond", 42];
const kv = ["key", 42];

function sumCount(s) {
  let sum = 0;
  let count = 0;
  for(let i of s) {
    sum += i;
    count += 1;
  }
  return [sum, count];
}
{% endhighlight %}
{% endcolumn %}
{% column 6 Scala %}
{% highlight scala %}
val t = ("James", "Bond", 42)
val kv = "key" -> 42 // same as ("key", 42)

def sumCount(s: Seq[Int]):(Int, Int) = {
  var sum = 0
  var count = 0
  for(i <- s) {
    sum += i
    count += 1
  }
  (sum, count)
}
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

To access values inside a tuple, use the `tuple._1` syntax, where the number indicates position within the tuple
(starting from 1, not 0). Quite often you can also use _destructuring_ to extract the values.

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
const sc = sumCount([1, 2, 3]);
const sum = sc[0];
const count = sc[1];

// with destructuring
const [sum, count] = sumCount([1, 2, 3]);
{% endhighlight %}
{% endcolumn %}
{% column 6 Scala %}
{% highlight scala %}
val sc = sumCount(Seq(1, 2, 3))
val sum = sc._1
val count = sc._2

// with destructuring
val (sum, count) = sumCount(Seq(1, 2, 3))
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

#### Seq

`Seq` is an ordered sequence. Typical implementations include `List`, `Vector`, `Buffer` and `Range`. Although Scala
`Array` is not a `Seq`, it can be wrapped into a `WrappedArray` to enable all `Seq` operations on arrays. In Scala this
is done automatically through an implicit conversion, allowing you to write code like following.

{% columns %}
{% column 9 Scala %}
{% highlight scala %}
val ar = Array(1, 2, 3, 4)
val product = ar.foldLeft(1)((a, x) => a * x) // foldLeft comes from WrappedArray
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

The `Seq` trait exposes many methods familiar to the users of JavaScript arrays, including `forEach`, `map`, `filter`,
`slice`, `find` and `reverse`. In addition to these, there are several more useful methods shown with examples in the
code block below.

{% columns %}
{% column 9 Scala %}
{% highlight scala %}
val ar = Seq(1, 2, 3, 4, 5)
ar.isEmpty == false
ar.forall(x => x > 0) == true // similar to JS Array.every()
ar.exists(x => x % 3 == 0) == true // similar to JS Array.some()
ar.head == 1
ar.last == 5
ar.tail == Seq(2, 3, 4, 5)
ar.init == Seq(1, 2, 3, 4)
ar.drop(2) == Seq(3, 4, 5)
ar.dropRight(2) == Seq(1, 2, 3)
ar.count(x => x < 3) == 2
ar.groupBy(x => x % 2) == Map(1 -> Seq(1, 3, 5), 0 -> Seq(2, 4))
ar.sortBy(x => -x) == Seq(5, 4, 3, 2, 1)
ar.partition(x => x > 3) == (Seq(4, 5), Seq(1, 2, 3))
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}

JavaScript `Array.reduce` functionality is covered by separate `reduceLeft` and `foldLeft` methods. The difference is
that in `foldLeft` you provide an initial ("zero") value (which is optional parameter to `Array.reduce`) and in 
`reduceLeft` you don't. Also note that in `foldLeft` the type of the accumulator can be something else, for example a
tuple, but in `reduceLeft` it must always be a supertype of the value. 

{% columns %}
{% column 6 ES6 %}
{% highlight javascript %}
function sumCount(s) {
  // use an array to represent a tuple
  return s.reduce(([sum, count], x) => 
    [sum + x, count + 1], 
    [0, 0]
  );
}
{% endhighlight %}
{% endcolumn %}
{% column 6 Scala %}
{% highlight scala %}
// use foldLeft to calculate sum and count
def sumCount(s: Seq[Int]):(Int, Int) = {
  // use a tuple accumulator to hold sum and count
  s.foldLeft((0, 0)){ case ((sum, count), x) => 
    (sum + x, count + 1) 
  }
}
{% endhighlight %}
{% endcolumn %}
{% endcolumns %}



-------------


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

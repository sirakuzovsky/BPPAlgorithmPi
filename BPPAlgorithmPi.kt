import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
import kotlin.CharArray

const val nhx = 16
fun main(){
  var pid: Double
  val id = 1000000
  val chx = CharArray(nhx)
/*  id is the digit position.  Digits generated follow immediately after id. */
  val s1 = series (1, id)
  val s2 = series (4, id)
  val s3 = series (5, id)
  val s4 = series (6, id)
  pid = 4.toDouble() * s1 - 2.toDouble() * s2 - s3 - s4
  pid = pid - pid.toInt() + 1.toDouble()
  ihex (pid, nhx, chx)
  println (" position = %d \n fraction = %.15f \n hex digits =  %10.10s \n".format(id,pid,chx.joinToString("")))
  /*position = 1000000
    fraction = 0.423429797567895
    hex digits =  6C65E52CB4
  */
}

/*  This returns, in chx, the first nhx hex digits of the fraction of x. */
fun ihex (x: Double, nhx: Int,  chx: CharArray){
  var y: Double
  val hx = "0123456789ABCDEF"
  y = abs (x)
  for (i in 0 until nhx){
    y = 16.toDouble() * (y - floor (y))
    chx[i] = hx[ y.toInt()]
  }
}

/*  This routine evaluates the series  sum_k 16^(id-k)/(8*k+m)
    using the modular exponentiation technique. */
fun series(m: Int, id: Int): Double{
  var ak: Double
  var p: Double
  var s: Double
  var t: Double
  val eps = 1e-17
  s = 0.toDouble()

/*  Sum the series up to id. */
  for (k in 0 until id){
    ak = (8 * k + m).toDouble()
    p = (id - k).toDouble()
    t = expm (p, ak)
    s += t / ak
    s -= s.toInt()
  }

/*  Compute a few terms where k >= id. */
  for (k in id .. id + 100){
    ak = (8 * k + m).toDouble();
    t = 16.toDouble().pow((id - k).toDouble()) / ak;
    if (t < eps) break;
    s += t;
    s -= s.toInt();
  }
  return s;
}

var tp1 = 0
const val ntp = 25
val tp = DoubleArray(ntp)

/*  expm = 16^p mod ak.  This routine uses the left-to-right binary
    exponentiation scheme. */
fun expm (p: Double, ak: Double): Double {
  var i: Int = 0
  var p1: Double
  var pt: Double
  var r: Double

/*  If this is the first call to expm, fill the power of two table tp. */
  if (tp1 == 0) {
    tp1 = 1
    tp[0] = 1.toDouble()
    for (ii in 1 until ntp) {i = ii;tp[ii] = 2.toDouble() * tp[ii-1]}
  }
  if (ak == 1.toDouble()) return 0.toDouble()

/*  Find the greatest power of two less than or equal to p. */
  for (ii in 0 until ntp){ i = ii; if (tp[ii] > p) break}
  pt = if (i > 0 ) tp[i-1] else tp[i]
  p1 = p
  r = 1.toDouble()

/*  Perform binary exponentiation algorithm modulo ak. */
  for (j in 1..i+1){
    if (p1 >= pt){
      r *= 16.toDouble()
      r -= (r / ak).toInt() * ak
      p1 -= pt
    }
      pt *= 0.5
    if (pt >= 1.toDouble()){
        r *= r
        r -= (r / ak).toInt() * ak
    }
  }
  return r;
}

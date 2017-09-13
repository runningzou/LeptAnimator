# 属性动画缺点与改进

## 1、缺点

属性动画用起来确实很爽，很多动画效果都可以使用属性动画实现。下面看几个典型的使用场景：

### 1.1、代码难复用

```java
view.animate()
	.traslationX(10,20)
	.alpha(0,1)
	.start();

Animator animator = ObjectAnimator.ofFloat(view,"TranslationX",10,20,30);
animator.start();
```

代码写起来很简洁，直观。但是**难以复用**，如果另一个地方我需要使用相同效果的动画，还得再写一次一模一样的代码。

### 1.2、无法获取尺寸信息

很多时候我们需要根据一些尺寸信息来做动画：

```java
public class MainActivity extends AppCompatActivity {

	private TextView mTextView;
	private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		 mTextView = (TextView)findViewById(R.id.txt);
		 mLinearLayout = (LinearLayout)findViewById(R.id.txt);

		 //mLinearLayout 是 mTextView 的父布局，
		 //移动 mTextView,使其距离 mLinearLayout 右侧 10px
		 int distance = mLinearLayout.getWidth() - 10 - mTextView.getRight();

		 mTextView.animate()
		 		.TranslationX(distance)
		 		.start();

    }
}

```

上面的代码并不能按预想的样子运行，因为 view 的绘制和 Activity的创建不在相同的线程，在 onCreate() 中无法获取 view 的尺寸信息。但是很多时候，我们需要一些尺寸信息来做动画。系统提供的 API，尺寸信息单位都是 px,而不是常用的 dp。

## 1.3、复杂动画实现不够简洁

AnimatorSet 可以将多个 Animator 一起或顺序执行，但是多个 AnimatorSet 要顺序执行，就只有这样写了：

```
 mAnimatorSet1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                     mAnimatorSet2.addListener(new Animator.AnimatorListener() {
                         @Override
                         public void onAnimationStart(Animator animator) {

                         }

                         @Override
                         public void onAnimationEnd(Animator animator) {
									mAnimatorSet3.start()
                         }

                         @Override
                         public void onAnimationCancel(Animator animator) {

                         }

                         @Override
                         public void onAnimationRepeat(Animator animator) {

                         }
                     });
                     mAnimatorSet2.start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            mAnimatorSet1.start();
```
可以看见，代码优点繁琐。

## 2、[解决方案](https://github.com/runningzou/LeptAnimator)
这是一个属性动画封装的库，解决了上面提到的问题，使用方法：

* 添加依赖

根目录build
```gradle
allprojects {
    repositories {
        jcenter()
        maven {
            url 'https://dl.bintray.com/zouzhihao/maven'
        }
    }
}
```
项目 build
```gradle
compile 'com.runningzou.leptanimator:library:0.0.1'
```

* 定义一个 LeptAnimator 子类

```java
public class SimpleAnimator extends LeptAnimator {

	//必须覆写该构造方法，view 表示需要做动画的控件和需要测量尺寸信息的控件
    public SimpleAnimator(View... view) {
        super(view);

    }

	//target 就是构造方法中传入的 view
	//这些 View 可以用于做动画或者测量尺寸
    @Override
    public AnimatorBuilder prepare(View... target) {

        	//AnimatorBuilder 提供了很多实用的方法
            return AnimatorBuilder
                    .animate(target[0]) //动画1
                    .translationX（100) //单位 dp
                    .ParentTop(10) //距离顶部10dp
                    .duration(1000)

                    .with(target[1]) //动画2，动画2与动画1同步执行
                    .leftof(target[3],10); //target[1]移动到 target[3] 的右边,距离为 10dp,仅支持对静止的 view 设置相对位置
                    .alpha(0,1)

                    .after(target[2]) //动画3，动画1，2执行完了，再执行动画3
                    .translationX（100);


    }
}

```

* 使用

```java

LeptAnimator animator = new SimpleAnimator(view1,view2,view3,view4).start();

animator.setStartListener(new ViewAnimatorListener.startListener() {
            @Override
            public void onAnimatorStart() {
                Log.d("tag","动画开始");
            }
        });

animator.setEndListener(new ViewAnimatorListener.endListener() {
            @Override
            public void onAnimatorEnd() {
                Log.d("tag","动画结束");
            }
        });
```

## 3、问题解决了吗？
###3.1、代码复用
这个封装库中，动画被封装成了一个个的类，例如示例中的 SimpleAnimator，使用的时候只需要 new 一个对象，并传入需要操作的 view 就可以了。用人肯定会说了，我用 xml 写动画，代码中再加载就可以了。动画用 xml 写我一直觉得不优雅,代码可以很容易做到事（几行代码就获取一个 Animator 实例），用 xml 写，除了要增加文件数、代码行数，还会导致增加 IO 操作，效率降低（读取文件，解析文件）。

### 3.2、无法获取尺寸信息
LeptAnimator 的 prepare 方法大概是这样运行的

```java
view.post(new Runnable(){
	public void run() {
		prepare();
	}
})
```
所以在准备动画的过程中（即 prepare 方法中），可以获取到 view 的尺寸信息。

例如：将 view View 贴近父布局右边界，间隔为 margin dp

```java

public class SimpleAnimator extends LeptAnimator {
    @Override
    public AnimatorBuilder prepare(View... targets) {
        View view = targets[0];
        View ParentView = (View) view.getParent();

        int distance = ParentView.getWidth() - view.getLeft() - view.getWidth() - DistanceUtil.dp2px(10);
        return new AnimatorBuilder()
                        .translationX(distance);
    }
}

```

现在就可以计算 view 的尺寸信息了，但是每次都要计算，还是颇显麻烦，所有这个库的 AnimatorBuilder 类提供了几个方法来简化你的尺寸计算

```
//将 View 贴近父布局顶部，间隔为 margin dp
public AnimatorBuilder parentTop(int margin)

//将 View 贴近父布局底部，间隔为 margin dp
public AnimatorBuilder parentBottom(int margin)

//将 View 贴近父布局左边界，间隔为 margin dp
public AnimatorBuilder parentLeft(int margin)

//将 View 贴近父布局右边界，间隔为 margin dp
public AnimatorBuilder parentRight(int margin)

//将 View 移动到 target 的左侧，间隔为 rigntMargin
public AnimatorBuilder leftof(View target, int rightMargin)
//类似的方法还有
public AnimatorBuilder rightof(View target, int margin)
public AnimatorBuilder topof(View target, int margin)
public AnimatorBuilder bottomof(View target, int margin)
```
库中所有距离的单位均为 dp。
有了这些方法，上面的代码就可以改为

```java
public class SimpleAnimator extends LeptAnimator {
    @Override
    public AnimatorBuilder prepare(View... targets) {
        return new AnimatorBuilder()
                        .parentRignt(10);
    }
}
```

利用这这些方法的组合，可以更容易地实现一些酷炫的效果。
### 3.3、复杂动画实现不够简洁
看看示例代码

```java
public class SimpleAnimator extends LeptAnimator {

	//必须覆写该构造方法，view 表示需要做动画的控件和需要测量尺寸信息的控件
    public SimpleAnimator(View... view) {
        super(view);

    }

	//target 就是构造方法中传入的 view
	//这些 View 可以用于做动画或者测量尺寸
    @Override
    public AnimatorBuilder prepare(View... target) {

        	//AnimatorBuilder 提供了很多实用的方法
            return AnimatorBuilder
                    .animate(target[0]) //动画1
                    .translationX（100) //单位 dp
                    .ParentTop(10) //距离顶部10dp
                    .duration(1000)

                    .with(target[1]) //动画2，动画2与动画1同步执行
                    .leftof(target[3],10); //target[1]移动到 target[3] 的右边,距离为 10dp,仅支持对静止的 view 设置相对位置
                    .alpha(0,1)

                    .after(target[2]) //动画3，动画1，2执行完了，再执行动画3
                    .translationX（100);


    }
}

```

* 通过 with 可以定义同步执行的动画
* 通过 after 可以定义顺序执行的动画


## 4、彩蛋
库中提供了一个有意思的功能

```
//设置动画执行的百分比，0.5 表示动画执行一半。
leptAnimator.setpercent(0.2);
```
这个功能可以实现一些有意思的效果，比如 demo 中的 ScrollActivity(具体见下一节的 gif图)

## 5、more
更多用法可以查看demo，下面是demo的效果图。

![](https://github.com/runningzou/LeptAnimator/blob/master/app/gif/2017-09-12%2016.50.54.gif?raw=true)

##6、感谢
库的实现过程参考了以下开源项目：

* [ViewAnimator](https://github.com/florent37/ViewAnimator)
* [AndroidViewAnimations](https://github.com/daimajia/AndroidViewAnimations)
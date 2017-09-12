#属性动画缺点与改进

##1、缺点

属性动画用起来确实很爽，很多动画效果都可以使用属性动画实现。下面看几个典型的使用场景：

###1.1、代码难复用

```java
view.animate()
	.traslationX(10,20)
	.alpha(0,1)
	.start();

Animator animator = ObjectAnimator.ofFloat(view,"TranslationX",10,20,30);
animator.start();
```

代码写起来很简洁，直观。但是**难以复用**，如果另一个地方我需要使用相同效果的动画，还得再写一次一模一样的代码。

###1.2、无法获取尺寸信息

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

##1.3、复杂动画实现不够简洁

多个 AnimatorSet 顺序播放

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
可以看见，代码都点繁琐。

##2、[解决方案](https://github.com/runningzou/LeptAnimator)
这是一个属性动画封装的库，使用方法：

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
        if (target.length >= 1) {
        		//AnimatorBuilder 提供了很多实用的方法
            return AnimatorBuilder
                    .animate(target[0])
                    .translationX（100) //单位 dp
                    .ParentTop(10) //距离顶部10dp
                    .duration(1000);
        } else {
            throw new RuntimeException("BounceAnimator needs at least one view");
        }

    }
}

```

* 使用

```java
new SimpleAnimator(view).start();
```

3、more
更多用法可以查看demo，下面是demo的效果图。
![](http://ww1.sinaimg.cn/mw690/419e5facgy1fjguthjnhhg20ro1hw1kz.gif)




import 'package:flutter/material.dart';
import 'package:show_fps/show_fps.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      showPerformanceOverlay: true,
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: ShowFPS(
          alignment: Alignment.bottomRight,
          visible: true,
          showChart: true,
          child: MyHomePage(title: 'Flutter Demo Home Page')),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  void didChangeDependencies() {
    double devicePixelRatio = MediaQuery.of(context).devicePixelRatio;
    var height = MediaQuery.of(context).size.width / 10;
    var width = height;
    var sizePx = (width * devicePixelRatio).floor();
    for (var index = 0; index < 10; index++) {
      precacheImage(
        Image.asset(
          GridRotareItem.getImage(index),
          height: height,
          width: width,
          cacheHeight: sizePx,
          cacheWidth: sizePx,
          fit: BoxFit.cover,
        ).image,
        context,
      );
    }
    super.didChangeDependencies();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: GridView.builder(
        itemCount: 200,
        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 10,
        ),
        itemBuilder: (context, index) {
          var animateIndex = index % 3;
          if (animateIndex == 0) {
            return GridRotareItem(index: index);
          }
          if (animateIndex == 1) {
            return GridFadeItem(index: index);
          }
          return GridScaleItem(index: index);
        },
      ),
    );
  }
}

class GridRotareItem extends StatefulWidget {
  final int index;

  const GridRotareItem({Key? key, required this.index}) : super(key: key);

  @override
  _GridRotareItemState createState() => _GridRotareItemState();

  static String getImage(int index) {
    var url = 'assets/images/${index % 20}.jpeg';
    return url;
  }
}

class _GridRotareItemState extends State<GridRotareItem>
    with SingleTickerProviderStateMixin {
  late AnimationController animationController;
  late double height;
  late double width;
  late int sizePx;

  @override
  void initState() {
    super.initState();
    animationController = AnimationController(
      vsync: this,
      duration: Duration(seconds: 5),
    );
    animationController.repeat(reverse: true);
  }

  @override
  void didChangeDependencies() {
    double devicePixelRatio = MediaQuery
        .of(context)
        .devicePixelRatio;
    height = MediaQuery
        .of(context)
        .size
        .width / 10;
    width = height;
    sizePx = (width * devicePixelRatio).floor();
    super.didChangeDependencies();
  }


  @override
  void dispose() {
    animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return RotationTransition(
      turns: Tween(begin: 0.0, end: 1.0).animate(animationController),
      child: Image.asset(
        GridRotareItem.getImage(widget.index),
        height: height,
        width: width,
        cacheHeight: sizePx,
        cacheWidth: sizePx,
        fit: BoxFit.cover,
      ),
    );
  }
}

class GridFadeItem extends StatefulWidget {
  final int index;

  const GridFadeItem({Key? key, required this.index}) : super(key: key);

  @override
  _GridFadeItemState createState() => _GridFadeItemState();

  static String getImage(int index) {
    var url = 'assets/images/${index % 20}.jpeg';
    return url;
  }
}

class _GridFadeItemState extends State<GridFadeItem>
    with SingleTickerProviderStateMixin {
  late AnimationController animationController;
  late double height;
  late double width;
  late int sizePx;

  @override
  void initState() {
    super.initState();
    animationController = AnimationController(
      vsync: this,
      duration: Duration(seconds: 5),
    );
    animationController.repeat();
  }

  @override
  void didChangeDependencies() {
    double devicePixelRatio = MediaQuery.of(context).devicePixelRatio;
    height = MediaQuery.of(context).size.width / 10;
    width = height;
    sizePx = (width * devicePixelRatio).floor();
    super.didChangeDependencies();
  }

  @override
  void dispose() {
    animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return FadeTransition(
      opacity: Tween(begin: 0.0, end: 1.0).animate(animationController),
      child: Image.asset(
        GridFadeItem.getImage(widget.index),
        height: height,
        width: width,
        cacheHeight: sizePx,
        cacheWidth: sizePx,
        fit: BoxFit.fill,
      ),
    );
  }
}

class GridScaleItem extends StatefulWidget {
  final int index;

  const GridScaleItem({Key? key, required this.index}) : super(key: key);

  @override
  _GridScaleItemState createState() => _GridScaleItemState();

  static String getImage(int index) {

    var url = 'assets/images/${index % 20}.jpeg' ;

    return url;
  }
}

class _GridScaleItemState extends State<GridScaleItem>
    with SingleTickerProviderStateMixin {
  late AnimationController animationController;
  late double height;
  late double width;
  late int sizePx;

  @override
  void initState() {
    super.initState();
    animationController = AnimationController(
      vsync: this,
      duration: Duration(seconds: 5),
    );
    animationController.repeat();
  }

  @override
  void didChangeDependencies() {
    double devicePixelRatio = MediaQuery.of(context).devicePixelRatio;
    height = MediaQuery.of(context).size.width / 10;
    width = height;
    sizePx = (width * devicePixelRatio).floor();
    super.didChangeDependencies();
  }

  @override
  void dispose() {
    animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return ScaleTransition(
      scale: Tween(begin: 0.0, end: 1.0).animate(animationController),
      child: Image.asset(
        GridScaleItem.getImage(widget.index),
        height: width,
        width: height,
        cacheHeight: sizePx,
        cacheWidth: sizePx,
        fit: BoxFit.cover,
      ),
    );
  }
}


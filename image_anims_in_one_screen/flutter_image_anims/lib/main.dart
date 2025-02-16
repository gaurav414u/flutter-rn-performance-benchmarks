import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

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
      home: MyHomePage(title: 'Flutter Demo Home Page'),
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
  int loadedImages = 0;
  final int totalImages = 200;
  static const reportFullyDrawnChannel =
      MethodChannel('com.example.flutter_image_anims_perf/reportFullyDrawn');

  void _onImageLoaded() {
    setState(() {
      loadedImages++;
      if (loadedImages == totalImages) {
        _reportFullyDrawn();
      }
    });
  }

  _reportFullyDrawn() async {
    print("sending custom fully drawn");
    try {
      await reportFullyDrawnChannel.invokeMethod<int>('reportFullyDrawn');
    } on PlatformException catch (e) {
      print(e);
    }
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
            return GridRotareItem(index: index, onImageLoaded: _onImageLoaded);
          }
          if (animateIndex == 1) {
            return GridFadeItem(index: index, onImageLoaded: _onImageLoaded);
          }
          return GridScaleItem(index: index, onImageLoaded: _onImageLoaded);
        },
      ),
    );
  }
}

class GridRotareItem extends StatefulWidget {
  final int index;
  final VoidCallback onImageLoaded;

  const GridRotareItem(
      {Key? key, required this.index, required this.onImageLoaded})
      : super(key: key);

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
    return RotationTransition(
      turns: Tween(begin: 0.0, end: 1.0).animate(animationController),
      child: Image.asset(
        GridRotareItem.getImage(widget.index),
        height: height,
        width: width,
        cacheHeight: sizePx,
        cacheWidth: sizePx,
        fit: BoxFit.cover,
        frameBuilder: (context, child, frame, wasSynchronouslyLoaded) {
          if ((frame != null && frame == 0) || wasSynchronouslyLoaded)
            widget.onImageLoaded();
          return child;
        },
      ),
    );
  }
}

class GridFadeItem extends StatefulWidget {
  final int index;
  final VoidCallback onImageLoaded;

  const GridFadeItem(
      {Key? key, required this.index, required this.onImageLoaded})
      : super(key: key);

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
        frameBuilder: (context, child, frame, wasSynchronouslyLoaded) {
          if ((frame != null && frame == 0) || wasSynchronouslyLoaded)
            widget.onImageLoaded();
          return child;
        },
      ),
    );
  }
}

class GridScaleItem extends StatefulWidget {
  final int index;
  final VoidCallback onImageLoaded;

  const GridScaleItem(
      {Key? key, required this.index, required this.onImageLoaded})
      : super(key: key);

  @override
  _GridScaleItemState createState() => _GridScaleItemState();

  static String getImage(int index) {
    var url = 'assets/images/${index % 20}.jpeg';

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
        frameBuilder: (context, child, frame, wasSynchronouslyLoaded) {
          if ((frame != null && frame == 0) || wasSynchronouslyLoaded)
            widget.onImageLoaded();
          return child;
        },
      ),
    );
  }
}

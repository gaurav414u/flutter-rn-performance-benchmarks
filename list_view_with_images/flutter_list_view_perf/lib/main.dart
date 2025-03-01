import 'dart:math';
import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';

Random rand = Random();

randomColor() {
  return Color((rand.nextDouble() * 0xFFFFFF).toInt() << 0).withOpacity(1.0);
}

const Duration DURATION_SCROOL = Duration(seconds: 500);

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      showPerformanceOverlay: false,
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: TestPage(title: 'Flutter Demo Home Page'),
    );
  }
}

class Item {
  int index;
  Color color;
  Item(this.index, this.color);
}

class TestPage extends StatelessWidget {
  TestPage({Key? key, required this.title}) : super(key: key);

  final String title;
  final ScrollController scrollController = ScrollController();

  @override
  Widget build(BuildContext context) {
    var data = genereteData();

    return Scaffold(
      body: Stack(
        children: <Widget>[
          ListView.separated(
            cacheExtent: 0.0,
            controller: scrollController,
            key: const ValueKey('long_list'),
            separatorBuilder: (context, int) {
              return const Divider();
            },
            itemCount: data.length,
            itemBuilder: (context, index) {
              return Cell(item: data[index]);
            },
          ),
          Padding(
            padding: const EdgeInsets.only(top: 32.0),
            child: SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: () async {
                  await scrollController.animateTo(1000.0 * (100 + 16),
                      duration: DURATION_SCROOL, curve: Curves.linear);
                },
                child: const Text('Start Scrolling'),
              ),
            ),
          ),
        ],
      ),
    );
  }

  List<Item> genereteData() {
    List<Item> data = [];
    for (int i = 0; i < 1001; i++) {
      data.add(Item(i, randomColor()));
    }
    return data;
  }
}

class Cell extends StatefulWidget {
  final Item item;

  Cell({Key? key, required this.item}) : super(key: key);

  @override
  _CellState createState() {
    return _CellState();
  }
}

class _CellState extends State<Cell> with TickerProviderStateMixin {
  late AnimationController rotationController;

  @override
  void initState() {
    super.initState();
    rotationController = AnimationController(
        duration: const Duration(milliseconds: 5000), vsync: this);
    SchedulerBinding.instance.addPostFrameCallback((_) {
      startAnimation();
    });
  }

  startAnimation() {
    rotationController.repeat(
        min: 0.0, max: 1.0, period: const Duration(milliseconds: 5000));
  }

  @override
  void dispose() {
    rotationController?.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    double devicePixelRatio = MediaQuery.of(context).devicePixelRatio;
    var sizePx = (100 * devicePixelRatio).floor();
    return Container(
      height: 100,
      color: widget.item.color,
      child: Row(
        children: <Widget>[
          Image.asset(
            getImage(widget.item.index),
            height: 100.0,
            width: 100.0,
            cacheHeight: sizePx,
            cacheWidth: sizePx,
            fit: BoxFit.fill,
          ),
          RotationTransition(
            turns: Tween(begin: 0.0, end: 1.0).animate(rotationController),
            child: Image.asset(
              getImage(widget.item.index),
              height: 100.0,
              width: 100.0,
              fit: BoxFit.fill,
              cacheHeight: sizePx,
              cacheWidth: sizePx,
            ),
          ),
          Center(
            child: Text(
              widget.item.index.toString(),
              key: ValueKey('item_${widget.item.index}_text'),
            ),
          ),
        ],
      ),
    );
  }

  static getImage(index) {
    var url = 'assets/images/${index % 20}.jpeg';
    return url;
  }
}

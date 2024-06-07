import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';

class BuyMeACoffee extends StatefulWidget {
  const BuyMeACoffee({super.key});

  @override
  State<BuyMeACoffee> createState() => _BuyMeACoffeeState();
}

class _BuyMeACoffeeState extends State<BuyMeACoffee> {
  @override
  Widget build(BuildContext context) {
    return Center(
      child: ElevatedButton.icon(
        icon: const Icon(Icons.coffee),
        label: const Text('Buy Me a Coffee'),
        style: ElevatedButton.styleFrom(
          backgroundColor: Colors.white,
        ),
        onPressed: _launchBuyMeACoffeeURL,
      ),
    );
  }

  void _launchBuyMeACoffeeURL() async {
    const url = 'https://www.buymeacoffee.com/yourprofile';
    if (await canLaunchUrl(url as Uri)) {
      await launchUrl(url as Uri);
    } else {
      throw 'Could not launch $url';
    }
  }
}


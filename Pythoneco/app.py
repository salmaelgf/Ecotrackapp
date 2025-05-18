from flask import Flask, jsonify
import pandas as pd
import joblib

app = Flask(__name__)  

# Charger le TSV à partir d’un échantillon ou en full si possible
df = pd.read_csv("en.openfoodfacts.org.products.tsv", sep='\t', dtype={'code': str}, low_memory=False)

regressor = joblib.load("carbon_footprint_regressor.pkl")
classifier = joblib.load("carbon_footprint_classifier.pkl")

# Colonnes nécessaires
columns_to_keep = [
    'code', 'product_name', 'brands', 'quantity', 'packaging',
    'categories', 'labels_tags', 'countries', 'energy_100g', 'fat_100g',
    'saturated-fat_100g', 'carbohydrates_100g', 'sugars_100g',
    'fiber_100g', 'proteins_100g', 'salt_100g', 'sodium_100g',
    'nutrition-score-fr_100g', 'nutrition_grade_fr', 'carbon-footprint_100g',
    'ingredients_text', 'additives_tags', 'ingredients_from_palm_oil_tags',
    'image_url', 'image_small_url'
]

df = df[columns_to_keep]

# Supprimer les lignes sans code
df = df.dropna(subset=["code"])

# Route d'API
@app.route("/product/<barcode>", methods=["GET"])
def get_product(barcode):
    print(f"[DEBUG] Barcode received: {barcode}")
    product_row = df[df['code'].astype(str) == barcode]
    print(f"[DEBUG] Number of matches: {len(product_row)}")

    if product_row.empty:
        print("[DEBUG] No matching product found.")
        return jsonify({"error": "Product not found"}), 404

    product_data = product_row.iloc[0].dropna().to_dict()
    print(f"[DEBUG] Product data returned: {product_data.get('product_name', 'No name')}")
    return jsonify(product_data)


@app.route("/predict/<barcode>", methods=["GET"])
def predict_product(barcode):
    product_row = df[df['code'].astype(str) == barcode]

    if product_row.empty:
        return jsonify({"error": "Product not found"}), 404

    row = product_row.iloc[0].fillna('')

    input_dict = {
        'nutrition-score-fr_100g': row['nutrition-score-fr_100g'],
        'categories': row['categories'],
        'packaging': row['packaging'],
        'brands': row['brands'],
        'ingredients_text': row['ingredients_text']
    }

    input_df = pd.DataFrame([input_dict])

    predicted_cf = regressor.predict(input_df)[0]
    predicted_class = classifier.predict(input_df)[0]

    return jsonify({
        "product_name": row.get('product_name', 'Unknown'),
        "predicted_carbon_footprint_100g": round(predicted_cf, 2),
        "predicted_impact_category": predicted_class
    })
# Démarrer l'app Flask
if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=5000)

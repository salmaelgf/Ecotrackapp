import pandas as pd
from sklearn.ensemble import RandomForestRegressor, RandomForestClassifier
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.preprocessing import OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
from sklearn.model_selection import train_test_split
import joblib

# Load dataset
file_path = "en.openfoodfacts.org.products.tsv" 
df = pd.read_csv(file_path, sep="\t", dtype={'code': str}, low_memory=False)

# Drop rows without target
df = df.dropna(subset=['carbon-footprint_100g', 'nutrition-score-fr_100g', 'categories', 'packaging', 'ingredients_text'])

# Define features and targets
features = ['nutrition-score-fr_100g', 'categories', 'packaging', 'ingredients_text']
target_reg = 'carbon-footprint_100g'

# Classification label
def label_impact(x):
    if x < 5:
        return 'low'
    elif x <= 15:
        return 'medium'
    else:
        return 'high'

df['impact_category'] = df['carbon-footprint_100g'].apply(label_impact)

# Preprocessing
preprocessor = ColumnTransformer(transformers=[
    ('cat1', OneHotEncoder(handle_unknown='ignore'), ['categories', 'packaging']),
    ('txt', TfidfVectorizer(max_features=100), 'ingredients_text')
], remainder='passthrough')  # Keep numeric features

# 1️⃣ Train Regressor
regressor_pipeline = Pipeline([
    ('preprocess', preprocessor),
    ('model', RandomForestRegressor(n_estimators=100, random_state=42))
])

X_reg = df[features]
y_reg = df[target_reg]
X_train_reg, X_test_reg, y_train_reg, y_test_reg = train_test_split(X_reg, y_reg, test_size=0.2, random_state=42)
regressor_pipeline.fit(X_train_reg, y_train_reg)

# Save regressor
joblib.dump(regressor_pipeline, "carbon_footprint_regressor.pkl")

# 2️⃣ Train Classifier
classifier_pipeline = Pipeline([
    ('preprocess', preprocessor),
    ('model', RandomForestClassifier(n_estimators=100, random_state=42))
])

y_clf = df['impact_category']
X_train_clf, X_test_clf, y_train_clf, y_test_clf = train_test_split(X_reg, y_clf, test_size=0.2, random_state=42)
classifier_pipeline.fit(X_train_clf, y_train_clf)

# Save classifier
joblib.dump(classifier_pipeline, "carbon_footprint_classifier.pkl")

print("✅ Models trained and saved successfully.")

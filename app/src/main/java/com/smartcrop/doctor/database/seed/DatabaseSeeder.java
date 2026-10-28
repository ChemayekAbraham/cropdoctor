package com.smartcrop.doctor.database.seed;

import android.util.Log;

import com.smartcrop.doctor.database.AppDatabase;
import com.smartcrop.doctor.database.entity.Crop;
import com.smartcrop.doctor.database.entity.DifferentialFeature;
import com.smartcrop.doctor.database.entity.Disease;
import com.smartcrop.doctor.database.entity.DiseaseReference;
import com.smartcrop.doctor.database.entity.ModelClass;
import com.smartcrop.doctor.database.entity.PreventionTip;
import com.smartcrop.doctor.database.entity.RecommendedInspection;
import com.smartcrop.doctor.database.entity.Symptom;
import com.smartcrop.doctor.database.entity.SymptomAnswerEffect;
import com.smartcrop.doctor.database.entity.SymptomQuestion;
import com.smartcrop.doctor.database.entity.Treatment;

import java.util.ArrayList;
import java.util.List;

/**
 * Seeds the Room SQLite database with initial agricultural data across 12 crops,
 * full disease catalogue, differential diagnosis traits, follow-up checklist questions,
 * and agronomic treatment guidance.
 */
public class DatabaseSeeder {

    private static final String TAG = "DatabaseSeeder";

    public static void seedDatabase(AppDatabase db) {
        if (db.cropDao().getCropCount() > 0) {
            Log.d(TAG, "Database already seeded. Skipping.");
            return;
        }

        Log.d(TAG, "Starting database seeding...");

        // 1. Seed 12 Crops
        List<Crop> crops = new ArrayList<>();
        crops.add(new Crop("maize", "Maize (Corn)", "Zea mays",
                "One of the world's most vital staple cereal crops, widely grown for grain and forage.",
                "Healthy maize leaves are long, slender, deep green with parallel veins and no discolorations or lesions.",
                "crop_maize", true));

        crops.add(new Crop("tomato", "Tomato", "Solanum lycopersicum",
                "Popular Solanaceae fruit vegetable highly sensitive to foliar fungal and bacterial blights.",
                "Healthy tomato foliage is vibrant green, compound, with serrated margins and free from yellowing halos.",
                "crop_tomato", true));

        crops.add(new Crop("cassava", "Cassava", "Manihot esculenta",
                "Drought-resistant tropical root crop, critical for food security across sub-Saharan Africa.",
                "Healthy cassava leaves have 5 to 7 deep green, smooth palmate lobes with no distortion or mosaic mottling.",
                "crop_cassava", true));

        crops.add(new Crop("beans", "Beans", "Phaseolus vulgaris",
                "Essential pulse crop providing protein and nitrogen fixation, vulnerable to rust and anthracnose.",
                "Healthy bean leaves are trifoliate, uniformly green with clean leaf blades and supple texture.",
                "crop_beans", true));

        crops.add(new Crop("potato", "Potato", "Solanum tuberosum",
                "Major starchy tuber crop susceptible to aggressive Phytophthora late blight and early blight.",
                "Healthy potato leaves are dark emerald green, slightly textured, with no necrosis or edge browning.",
                "crop_potato", true));

        crops.add(new Crop("groundnuts", "Groundnuts (Peanut)", "Arachis hypogaea",
                "Legume cash crop grown for edible seeds and high-quality cooking oil.",
                "Healthy groundnut foliage displays pinnate leaves with four obovate leaflets, bright green without spots.",
                "crop_groundnuts", true));

        crops.add(new Crop("banana", "Banana", "Musa acuminata",
                "Perennial herbaceous giant crop, heavily impacted by fungal leaf spot complex and bacterial wilts.",
                "Healthy banana leaves are expansive, paddle-shaped, waxy green with intact margins and no chlorotic streaks.",
                "crop_banana", false));

        crops.add(new Crop("coffee", "Coffee", "Coffea arabica / canephora",
                "Global economic beverage tree crop severely threatened by Hemileia vastatrix leaf rust.",
                "Healthy coffee leaves are shiny, waxy dark green with prominent pinnate venation and no powdery undersides.",
                "crop_coffee", true));

        crops.add(new Crop("rice", "Rice", "Oryza sativa",
                "Primary staple grain for over half the world population, prone to leaf blast and bacterial blight.",
                "Healthy rice leaves are linear, erect, clear lime-green with smooth edges and no spindle lesions.",
                "crop_rice", true));

        crops.add(new Crop("pepper", "Pepper (Capsicum)", "Capsicum annuum",
                "High-value spice and vegetable crop vulnerable to bacterial leaf spot and anthracnose fruit lesions.",
                "Healthy pepper leaves are glossy, lanceolate, uniformly olive green without necrotic spotting.",
                "crop_pepper", true));

        crops.add(new Crop("sorghum", "Sorghum", "Sorghum bicolor",
                "Resilient dryland cereal grain suited for arid climates, susceptible to anthracnose and leaf rust.",
                "Healthy sorghum leaves are waxy, broad, bluish-green with clear central midrib and no reddish lesions.",
                "crop_sorghum", false));

        crops.add(new Crop("sweet_potato", "Sweet Potato", "Ipomoea batatas",
                "Nutritious vine root crop susceptible to viral complexes and Alternaria leaf blight.",
                "Healthy sweet potato leaves are heart-shaped or lobed, lush green with supple stems and no vein clearing.",
                "crop_sweet_potato", false));

        db.cropDao().insertCrops(crops);

        // 2. Seed Diseases Catalogue
        List<Disease> diseases = new ArrayList<>();

        // MAIZE DISEASES
        diseases.add(new Disease("maize_northern_leaf_blight", "maize", "Northern Leaf Blight",
                "Exserohilum turcicum", "Fungal",
                "A damaging foliar disease thriving in moderate temperatures and high humidity, causing extensive photosynthetic area loss.",
                "Overwinters in crop debris; airborne conidia spread rapidly during extended dews (18-27°C).",
                "Large, elongated, cigar-shaped grayish-green to tan lesions (2.5 to 15 cm long) with smooth margins.",
                "Distinctive cigar-like spindle shape, often with dark olive fungal sporulation in damp conditions.",
                "High",
                "Rotate with non-host crops, bury or remove infected crop residue, choose resistant hybrid varieties.",
                "Select certified resistant cultivars, avoid high plant density to improve air circulation.",
                "Fungicides (e.g. strobilurins or triazoles) may be applied early if threshold is reached in high-yield fields. Follow local label instructions and wear protective gear.",
                "Inspect lower leaves first at tasseling stage; monitor lesion expansion weekly.",
                "Seek agronomic assistance if lesions appear before pollination on upper ear leaves across >30% of field.",
                "CABI Crop Protection Compendium; CIMMYT Maize Pathology Manual.",
                "2026-01-15", true, "maize_northern_leaf_blight", true));

        diseases.add(new Disease("maize_common_rust", "maize", "Common Rust",
                "Puccinia sorghi", "Fungal",
                "Foliar rust disease causing chlorosis and leaf death under cool, moist environmental conditions.",
                "Spreads via airborne urediniospores blown across long distances.",
                "Golden-brown to cinnamon-brown powdery pustules on both upper and lower leaf surfaces.",
                "Raised powdery pustules that rub off reddish-brown dust onto fingers when touched.",
                "Moderate",
                "Plant rust-resistant hybrids; practice timely planting to avoid high disease pressure periods.",
                "Maintain balanced nitrogen fertilization; avoid excessive planting density.",
                "Chemical control rarely economic unless on seed corn or early severe infection. Ensure registered products only.",
                "Check upper and lower leaf surfaces, particularly middle canopy leaves.",
                "Refer to extension if pustules appear densely on leaves above the ear early in the season.",
                "FAO Plant Production Bulletin; Pioneer Agronomy Guide.",
                "2026-01-15", true, "maize_common_rust", true));

        diseases.add(new Disease("maize_gray_leaf_spot", "maize", "Gray Leaf Spot",
                "Cercospora zeae-maydis", "Fungal",
                "Severe foliar disease causing significant yield reductions in no-till and continuous corn systems.",
                "Fungus survives in surface residue; high relative humidity and warm temperatures favor infection.",
                "Tan, strictly rectangular lesions bounded cleanly between secondary leaf veins.",
                "Strict rectangular lesion geometry running parallel to leaf veins with sharp boundaries.",
                "High",
                "Tillage where conservation allows, 2-year crop rotation, planting tolerant hybrids.",
                "Ensure proper row spacing; eliminate volunteer maize plants.",
                "Timely fungicide application at VT-R1 growth stages if weather is humid and hybrid is susceptible.",
                "Scout lower third of canopy starting 2 weeks before tasseling.",
                "Report if lesions coalesce and burn upper canopy leaves during grain fill.",
                "Iowa State University Extension; CABI Plantwise.",
                "2026-01-15", true, "maize_gray_leaf_spot", true));

        diseases.add(new Disease("maize_streak_virus", "maize", "Maize Streak Virus (MSV)",
                "Maize streak virus (Geminiviridae)", "Viral",
                "Major viral disease of maize in Africa transmitted persistently by leafhoppers (Cicadulina spp.).",
                "Leafhoppers transmit the virus while feeding; prevalent after sporadic early rains.",
                "Continuous or interrupted chlorotic streaks running parallel to veins along entire leaf blade.",
                "Fine, uniform chlorotic white-yellow stripes down the leaf veins; plants often stunted.",
                "Critical",
                "Rogue infected seedlings early; control leafhopper vector; plant certified MSV-resistant seeds.",
                "Synchronize planting dates within community; avoid planting downwind of older cereal fields.",
                "Fungicides do NOT cure viral infections! Vector management at planting with approved seed dressings.",
                "Examine new unfurling leaves and check for presence of small active leafhoppers.",
                "Consult local extension officer immediately if stunting occurs across >15% of young crop.",
                "IITA Maize Health Guide; CABI MSV Datasheet.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("maize_healthy", "maize", "Healthy Maize Leaf",
                "N/A", "Healthy",
                "Maize foliage shows robust vigor, normal photosynthetic green pigmentation, and absence of necrotic lesions or pustules.",
                "Optimal soil nutrition, proper moisture, and absence of significant pathogen pressure.",
                "Uniform green coloration, intact leaf margins, clear linear venation, absence of spots or streaks.",
                "No visible lesion, chlorosis, distortion, or sporulation.",
                "Normal",
                "Maintain good agronomic stewardship, weed management, and balanced nutrient topdressing.",
                "Continue routine weekly field monitoring across different field quadrants.",
                "No chemical treatment required.",
                "Regular weekly visual field walkthrough.",
                "None needed unless new abnormalities arise.",
                "FAO Good Agricultural Practices Guide.",
                "2026-01-15", true, "maize_healthy", true));

        // TOMATO DISEASES
        diseases.add(new Disease("tomato_early_blight", "tomato", "Early Blight",
                "Alternaria solani", "Fungal",
                "Common foliar fungal disease of tomatoes causing premature defoliation and stem cankers.",
                "Soil-borne pathogen spreading through rain splash, warm temperatures (24-29°C), and wet foliage.",
                "Dark brown to black spots with characteristic concentric rings, surrounded by chlorotic yellow halos.",
                "Concentric target-like rings within larger brown spots, primarily originating on older lower leaves.",
                "Moderate to High",
                "Prune lower infected leaves, stake plants to elevate foliage off soil, apply straw mulch.",
                "3-year rotation away from Solanaceae; drip irrigate instead of overhead sprinkling.",
                "Apply approved protective copper-based or chlorothalonil fungicides at first sign. Wear PPE.",
                "Inspect the lowest 30 cm of plant foliage and stem joints weekly.",
                "Refer to specialist if stem girdling lesions develop on young transplants.",
                "Cornell Vegetable MD Online; University of Florida Extension.",
                "2026-01-15", true, "tomato_early_blight", true));

        diseases.add(new Disease("tomato_late_blight", "tomato", "Late Blight",
                "Phytophthora infestans", "Oomycete",
                "Highly destructive, fast-moving oomycete that can devastate an entire field in days under cool, wet conditions.",
                "Airborne sporangia spread rapidly during cool, foggy, overcast weather (15-20°C with >90% humidity).",
                "Large, irregular water-soaked pale-green to brown lesions rapidly expanding into greasy black necrosis.",
                "White, downy cottony mold on the underside of infected leaves in humid conditions; greasy brown stems.",
                "Critical",
                "Remove and destroy infected plants immediately (do not compost); burn or bury deeply.",
                "Plant certified disease-free transplants; destroy volunteer tomato and potato cull piles.",
                "Preventative fungicide applications (mancozeb, copper, or systemic oomycides) are essential before heavy rains. Strictly adhere to pre-harvest intervals.",
                "Check entire canopy every 2-3 days during cool wet spells, focusing on leaf undersides and stems.",
                "Notify regional agricultural extension office immediately upon first suspected outbreak.",
                "EuroBlight Network; USDA ARS Late Blight Database.",
                "2026-01-15", true, "tomato_late_blight", true));

        diseases.add(new Disease("tomato_septoria_leaf_spot", "tomato", "Septoria Leaf Spot",
                "Septoria lycopersici", "Fungal",
                "Destructive foliar disease causing severe lower leaf defoliation, exposing fruits to sunscald.",
                "Fungal pycnidia overwinter in weed solanaceous hosts and crop residue; spread by water splashing.",
                "Numerous small circular spots (1.5 to 3 mm) with distinct dark borders and pale gray or tan centers.",
                "Tiny dark speckling (pycnidia fruiting bodies like ground black pepper) in the center of pale spots.",
                "Moderate",
                "Strip off lower infected leaves promptly; avoid overhead irrigation; clean garden stakes.",
                "Keep beds weed-free; rotate crops for at least two years; maintain wide plant spacing.",
                "Apply protective copper or mancozeb sprays at initial symptom appearance. Observe safety intervals.",
                "Lower canopy leaves and stems; observe spot diameter and check for central black fruiting dots.",
                "Consult extension if defoliation reaches above mid-canopy before fruit set.",
                "Ohio State University Extension; Plantwise Factsheet.",
                "2026-01-15", true, "tomato_septoria_leaf_spot", true));

        diseases.add(new Disease("tomato_bacterial_spot", "tomato", "Bacterial Spot",
                "Xanthomonas perforans / euvesicatoria", "Bacterial",
                "Bacterial infection causing necrotic spots on leaves, stems, and unmarketable scabby spots on fruit.",
                "Seed-borne bacteria spreading via wind-driven rain, overhead sprinklers, and handling wet plants.",
                "Small angular dark-brown to black spots, often greasy or water-soaked initially, with yellow halos.",
                "Angular, vein-delimited lesions, often shot-hole or tearing in leaf blades; scabby fruit spots.",
                "High",
                "Do not work in fields when leaves are wet; rogue diseased seedlings; sanitize pruning shears.",
                "Use certified pathogen-free seeds (hot water treated); avoid solanaceous crop debris.",
                "Copper fungicides mixed with mancozeb can reduce bacterial spread, though resistance occurs. Fungicides do not cure bacterial infections.",
                "Inspect both young expanding leaves and developing green fruits.",
                "Seek confirmation if blister-like raised scabs appear on commercial green tomato fruits.",
                "UC Davis IPM Guidelines; WorldVeg AVRDC Tomato Disease Guide.",
                "2026-01-15", true, "tomato_bacterial_spot", true));

        diseases.add(new Disease("tomato_mosaic_virus", "tomato", "Tomato Mosaic Virus (ToMV)",
                "Tomato mosaic virus", "Viral",
                "Highly infectious, mechanically transmitted tobamovirus that causes mottling and fruit distortion.",
                "Transmitted easily by contaminated hands, tools, clothing, grafting, and infected seed.",
                "Mottling with alternating light and dark green areas, leaf curling, distortion, and 'shoestring' leaves.",
                "Fern-like or shoestring leaf deformation with blistered dark-green patches.",
                "High",
                "Rogue out and burn infected plants immediately; wash hands with milk or soap after handling.",
                "Select resistant cultivars (labeled with 'T' or 'ToMV'); ban tobacco smoking near tomato greenhouses.",
                "Chemical pesticides have NO effect on viruses! Focus strictly on sanitization and resistant seeds.",
                "Inspect terminal shoots, young leaflets, and internal fruit walls for brown necrosis.",
                "Contact extension specialist to test seed lots if multiple plants show simultaneous symptoms.",
                "Plant Pathology Circular No. 344; Royal Horticultural Society.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("tomato_healthy", "tomato", "Healthy Tomato Leaf",
                "N/A", "Healthy",
                "Healthy tomato plant with well-developed leaves, robust turgor pressure, and clear green leaf blades.",
                "Balanced irrigation, optimal fertility, and effective preventative pest/disease management.",
                "Vibrant green leaves, uniform color, crisp margins, no spotting, browning, or curling.",
                "Absence of chlorotic spots, dark rings, water-soaking, or white sporulation.",
                "Normal",
                "Continue standard cultural practices, balanced N-P-K feeding, and preventative scouting.",
                "Maintain drip irrigation and preventative mulching.",
                "No treatment required.",
                "Weekly scouting of lower leaves for early pest or disease incursions.",
                "None needed.",
                "FAO Vegetable Production Handbook.",
                "2026-01-15", true, "tomato_healthy", true));

        // CASSAVA DISEASES
        diseases.add(new Disease("cassava_mosaic_disease", "cassava", "Cassava Mosaic Disease (CMD)",
                "African cassava mosaic virus (ACMV)", "Viral",
                "Widespread viral disease in Africa responsible for dramatic yield losses in cassava storage roots.",
                "Transmitted by the whitefly Bemisia tabaci and distributed through infected stem cuttings.",
                "Yellow and green chlorotic mosaic pattern, distorted, twisted and stunted leaflets.",
                "Severe leaf puckering and asymmetry where one side of a leaflet lobe is much smaller than the other.",
                "Critical",
                "Plant only certified virus-free cuttings; rogue infected plants younger than 2 months.",
                "Use high-yielding CMD-resistant varieties bred by IITA and national breeding programs.",
                "Fungicides are completely ineffective. Control whiteflies with integrated pest management.",
                "Inspect young expanding leaves on the upper third of the plant.",
                "Notify district agricultural officer if certified resistant variety displays severe mosaic.",
                "IITA Cassava Disease Field Guide; CABI CMD Datasheet.",
                "2026-01-15", true, "cassava_mosaic_disease", true));

        diseases.add(new Disease("cassava_brown_streak_disease", "cassava", "Cassava Brown Streak Disease (CBSD)",
                "Cassava brown streak virus (CBSV)", "Viral",
                "Insidious viral disease causing root rot and brown streaks, devastating root quality while leaves may look subtle.",
                "Spread by whitefly vectors and propagation of infected stem cuttings.",
                "Feathery yellowing along secondary veins, brown necrotic streaks on green stems, dry corky root rot.",
                "Feathery chlorosis bordering leaf veins on older leaves; yellowing without leaf distortion; brown internal root rot.",
                "Critical",
                "Uproot and destroy infected plants; do not use stems from affected plants for propagation.",
                "Plant clean certified planting material from official seed inspection agencies.",
                "Chemicals do not cure viruses. Leaf diagnosis alone is insufficient; root inspection is vital.",
                "Examine older lower leaves, stems for brown lesions, and slice open roots to check for corky brown rot.",
                "Immediate referral required; CBSD quarantine regulations apply in many jurisdictions.",
                "FAO Cassava Pest Management; IITA CBSD Manual.",
                "2026-01-15", true, "cassava_brown_streak_disease", false));

        diseases.add(new Disease("cassava_bacterial_blight", "cassava", "Cassava Bacterial Blight (CBB)",
                "Xanthomonas axonopodis pv. manihotis", "Bacterial",
                "Major bacterial disease causing water-soaked angular leaf spots, blight, gum exudation, and dieback.",
                "Bacteria enter via stomata or wounds; spread by rain splash, contaminated machetes, and planting material.",
                "Angular water-soaked leaf spots, wilting, leaf blighting, sticky gum droplets on stems, shoot dieback.",
                "Characteristic angular water-soaked leaf spots turning brown, often accompanied by amber gum droplets on young stems.",
                "High",
                "Prune dead tips with disinfected shears; rogue diseased stems; harvest early if roots are mature.",
                "Use healthy planting stems from clean fields; plant resistant cultivars; practice crop rotation.",
                "No effective chemical cures. Copper bactericides provide limited suppression in nursery beds.",
                "Scout young stems and examine leaf undersides early morning for water-soaking and exudate.",
                "Report if sudden dieback affects newly planted cassava fields.",
                "CGIAR Research Program on Roots, Tubers and Bananas.",
                "2026-01-15", true, "cassava_bacterial_blight", true));

        diseases.add(new Disease("cassava_anthracnose", "cassava", "Cassava Anthracnose",
                "Colletotrichum gloeosporioides", "Fungal",
                "Fungal disease causing cankers on green stems, leaf spots, and tip dieback, often linked to bug punctures.",
                "Pathogen enters via feeding punctures made by the pseudotheraptus bug; favored by warm rain.",
                "Oval sunken cankers on green stems, brown leaf lesions with yellow halos, defoliation.",
                "Deep sunken cankers at petiole bases and stem nodes causing brittle snapping of stems.",
                "Moderate",
                "Prune and destroy infected stems; control sap-sucking insects in the field.",
                "Intercrop with legumes to reduce pest density; use tolerant varieties.",
                "Fungicides rarely recommended for field cassava; focus on cultural control.",
                "Inspect green stems between leaf scars and look for insect feeding marks.",
                "Consult extension if stem breakage is prevalent across the plantation.",
                "IITA Plant Health Management Series.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("cassava_healthy", "cassava", "Healthy Cassava Leaf",
                "N/A", "Healthy",
                "Normal vigorous cassava foliage with fully expanded green lobes and no signs of distortion.",
                "Pathogen-free planting material and adequate field fertility.",
                "Uniform green palmate leaves with smooth margins and clean petioles.",
                "Absence of yellow mosaic, vein chlorosis, angular spots, or stem cankers.",
                "Normal",
                "Maintain weed control during first 3 months; scout regularly.",
                "Source clean certified cuttings for subsequent planting seasons.",
                "No treatment required.",
                "Bi-weekly field walking.",
                "None needed.",
                "FAO Good Agricultural Practices for Cassava.",
                "2026-01-15", true, "cassava_healthy", true));

        // BEANS DISEASES
        diseases.add(new Disease("bean_angular_leaf_spot", "beans", "Angular Leaf Spot",
                "Pseudocercospora griseola", "Fungal",
                "Important foliar and pod disease in warm, humid bean growing zones.",
                "Fungus carried in seed and crop residue; dispersed by splashing rain and wind.",
                "Angular gray to brown lesions strictly delimited by small leaf veins, giving a checkerboard appearance.",
                "Sharp geometric angular borders delimited by veins; fuzzy gray fungal growth on leaf undersides in humidity.",
                "High",
                "Plow under or burn infected bean residue after harvest; rotate crops for at least 2 years.",
                "Use certified disease-free seed; avoid working in fields when foliage is wet.",
                "Foliar fungicides (azoxystrobin, chlorothalonil, or copper) may be used at flowering stage.",
                "Examine lower and middle leaves and developing pods for sunken round spots.",
                "Refer to extension if premature defoliation occurs prior to pod fill.",
                "CIAT Bean Program; CABI Plantwise.",
                "2026-01-15", true, "bean_angular_leaf_spot", true));

        diseases.add(new Disease("bean_rust", "beans", "Bean Rust",
                "Uromyces appendiculatus", "Fungal",
                "Common foliar fungal disease that causes severe yield losses by decreasing photosynthetic area.",
                "Airborne rust spores; favored by moderate temperatures (17-27°C) and over 10 hours of leaf wetness.",
                "Small, elevated circular pustules containing reddish-brown powdery spores on leaves and pods.",
                "Raised reddish-brown pustules, often surrounded by yellow halos, that release powdery spores upon rubbing.",
                "Moderate to High",
                "Plant rust-resistant cultivars; destroy volunteer bean plants; practice crop rotation.",
                "Do not plant adjacent to older infected bean plots; maintain wider row spacing.",
                "Apply sulfur-based or registered triazole fungicides at onset of pustule formation.",
                "Check undersides of primary and trifoliate leaves.",
                "Notify specialist if pustules emerge during pre-flowering stage.",
                "FAO Crop Protection Bulletin; CIAT Pathology.",
                "2026-01-15", true, "bean_rust", true));

        diseases.add(new Disease("bean_anthracnose", "beans", "Bean Anthracnose",
                "Colletotrichum lindemuthianum", "Fungal",
                "Destructive seed-borne fungal disease causing dark brick-red to black necrotic lesions on veins and pods.",
                "Seed-borne and debris-borne; spreads rapidly during cool, wet weather (13-26°C).",
                "Dark purple-brown to black necrosis along veins on the underside of leaves; sunken pod cankers.",
                "Brick-red to black discoloration highlighting leaf veins on the underside of leaves.",
                "High",
                "Plant only certified disease-free western-grown seed; 3-year crop rotation.",
                "Destroy infected crop residues after harvest; never save seed from infected bean fields.",
                "Seed treatment with approved fungicides; foliar sprays during vegetative growth if wet weather persists.",
                "Examine the undersides of leaves along veins and check green pods for circular sunken lesions.",
                "Consult extension officer if sunken cankers with pink spore masses appear on pods.",
                "Michigan State University Extension; CIAT.",
                "2026-01-15", true, "bean_anthracnose", true));

        diseases.add(new Disease("bean_common_bacterial_blight", "beans", "Common Bacterial Blight (CBB)",
                "Xanthomonas phaseoli", "Bacterial",
                "Serious seed-borne bacterial infection causing large water-soaked leaf blights and yellow margins.",
                "Warm temperatures (above 28°C) and rainfall; spread by rain splash and contaminated implements.",
                "Water-soaked spots expanding into large brown necrotic patches surrounded by broad bright yellow margins.",
                "Large irregular burnt-looking leaf patches with broad, vivid yellow halos; greasy pod spots.",
                "High",
                "Use certified disease-free seed; avoid sprinkler irrigation; do not cultivate while wet.",
                "Rotate with non-host crops like maize or sorghum; destroy bean stubble.",
                "Copper hydroxide sprays can reduce secondary spread if applied early. No cure exists.",
                "Check leaf margins, upper leaves, and green pods for greasy sunken spots.",
                "Notify extension if blighting causes widespread defoliation in commercial seed crops.",
                "University of Nebraska Extension; Plantwise Factsheet.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("bean_healthy", "beans", "Healthy Bean Leaf",
                "N/A", "Healthy",
                "Normal healthy bean plant with well-developed trifoliate leaves and no signs of spotting or necrosis.",
                "Clean certified seed, fertile soil, and optimal moisture conditions.",
                "Trifoliate leaves uniformly green, supple, with smooth edges and no discoloration.",
                "No rust pustules, angular spots, vein necrosis, or yellow burning.",
                "Normal",
                "Ensure timely weeding and proper soil hilling.",
                "Practice regular field scouting for early pest detection.",
                "No treatment required.",
                "Weekly scouting.",
                "None needed.",
                "CIAT Good Agronomic Practices.",
                "2026-01-15", true, "bean_healthy", true));

        // POTATO DISEASES
        diseases.add(new Disease("potato_early_blight", "potato", "Early Blight",
                "Alternaria solani", "Fungal",
                "Frequent foliar disease on potato causing early senescence and tuber yield loss.",
                "Alternaria fungus surviving in solanaceous debris; favored by alternating wet and dry periods.",
                "Dark brown circular spots with concentric target-like rings, surrounded by yellow tissue.",
                "Concentric rings resembling an archery target inside brown spots on older lower leaves.",
                "Moderate",
                "Maintain optimal plant nutrition (especially nitrogen and potassium); drip irrigate.",
                "Allow 3-year rotation away from potatoes and tomatoes; destroy crop refuse.",
                "Apply protectant fungicides (mancozeb, chlorothalonil) starting before row closure.",
                "Lower canopy foliage and petiole junctions.",
                "Refer to extension if spots spread aggressively to upper leaves during early tuber bulk.",
                "Potato Association of America; University of Idaho Extension.",
                "2026-01-15", true, "potato_early_blight", true));

        diseases.add(new Disease("potato_late_blight", "potato", "Late Blight",
                "Phytophthora infestans", "Oomycete",
                "The historic cause of the Irish potato famine; extremely aggressive and destructive to potato crops.",
                "Airborne sporangia; favored by high humidity (>90%) and cool temperatures (15-20°C).",
                "Water-soaked lesions expanding rapidly into dark brown/black necrotic patches; white fuzzy mold below.",
                "White fungal-like sporulation on the underside of lesion margins under high humidity; foul odor in severe cases.",
                "Critical",
                "Destroy infected plants immediately; kill vines before harvest to prevent tuber infection.",
                "Plant certified certified seed tubers; destroy volunteer potatoes and cull piles.",
                "Strict preventative fungicide schedule during favorable weather. Systemic fungicides when risk is high.",
                "Scout fields twice weekly during wet cool periods, inspecting leaf undersides and stems.",
                "Immediate notification to potato grower alert networks.",
                "International Potato Center (CIP); EuroBlight.",
                "2026-01-15", true, "potato_late_blight", true));

        diseases.add(new Disease("potato_virus_y", "potato", "Potato Virus Y (PVY)",
                "Potato virus Y (Potyviridae)", "Viral",
                "Crucial viral pathogen transmitted by aphids causing mosaic, vein necrosis, and tuber necrosis.",
                "Non-persistently transmitted by numerous aphid species; carried in infected seed tubers.",
                "Mild to severe leaf mosaic, rugosity, leaf dropping ('leaf drop symptom'), and stunting.",
                "Necrotic streaks on the underside of leaf veins leading to dead leaves hanging onto stems.",
                "High",
                "Rogue infected plants early; use mineral oil sprays to interfere with aphid transmission.",
                "Plant only certified class-tested seed potatoes; isolate seed crops from commercial ware crops.",
                "Insecticides do not work fast enough to stop aphid transmission of PVY. Fungicides are useless.",
                "Examine growing tips for mosaic and lower leaves for vein necrosis.",
                "Seek laboratory ELISA or PCR testing for seed certification validation.",
                "CIP Pathology Manual; USDA ARS Potato Research.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("potato_healthy", "potato", "Healthy Potato Leaf",
                "N/A", "Healthy",
                "Healthy potato foliage with deep green color, vigorous growth, and no disease symptoms.",
                "High-quality seed tubers, balanced soil nutrition, and clean field management.",
                "Lush green compound leaves with crisp margins and healthy stems.",
                "Absence of target spots, water-soaked blights, or mosaic mottling.",
                "Normal",
                "Continue proper hilling and uniform irrigation.",
                "Monitor for Colorado potato beetle and aphid vectors.",
                "No treatment required.",
                "Routine scouting.",
                "None needed.",
                "CIP Potato Production Guidelines.",
                "2026-01-15", true, "potato_healthy", true));

        // GROUNDNUTS (PEANUT) DISEASES
        diseases.add(new Disease("groundnut_early_leaf_spot", "groundnuts", "Early Leaf Spot",
                "Passalora arachidicola", "Fungal",
                "Common foliar disease appearing earlier in the season, causing defoliation and pod yield drop.",
                "Spreads via airborne and splash-dispersed conidia from soil crop debris.",
                "Circular reddish-brown to dark brown spots surrounded by a prominent bright yellow halo.",
                "Distinct bright yellow halos around circular brown spots on upper leaf surfaces.",
                "Moderate to High",
                "Crop rotation with cereals (maize, sorghum); deep burial of peanut residue.",
                "Early planting of tolerant cultivars; avoid continuous groundnut cropping.",
                "Foliar fungicides (chlorothalonil, tebuconazole) applied at 14-day intervals if pressure is high.",
                "Check lower canopy leaves around 30-40 days after emergence.",
                "Refer to extension if defoliation exceeds 25% during pod development.",
                "ICRISAT Peanut Disease Handbook; CABI Plantwise.",
                "2026-01-15", true, "groundnut_early_leaf_spot", true));

        diseases.add(new Disease("groundnut_late_leaf_spot", "groundnuts", "Late Leaf Spot",
                "Nothopassalora personata", "Fungal",
                "Aggressive foliar disease occurring later in the growing cycle, often more destructive than early leaf spot.",
                "Thrives in humid conditions with prolonged dew; surviving in soil debris.",
                "Small circular dark brown to almost black spots, usually lacking the distinct yellow halo.",
                "Carbon-black spots on leaf undersides with cushion-like tufts of fungal fruiting structures.",
                "High",
                "Crop rotation; prompt harvest when mature to minimize pod loss.",
                "Plant modern resistant groundnut varieties bred by ICRISAT.",
                "Fungicide sprays as for early leaf spot; ensure coverage of leaf undersides.",
                "Examine lower surface of middle and lower canopy leaves.",
                "Consult extension if heavy leaf shedding leaves bare stems before pod maturity.",
                "ICRISAT Groundnut Pathology Manual.",
                "2026-01-15", true, "groundnut_late_leaf_spot", true));

        diseases.add(new Disease("groundnut_rust", "groundnuts", "Groundnut Rust",
                "Puccinia arachidis", "Fungal",
                "Destructive rust disease that causes leaves to dry up and curl, drastically curtailing pod fill.",
                "Airborne urediniospores, favored by humid, warm conditions.",
                "Orange to reddish-brown pustules predominantly on the lower leaf surface.",
                "Pustules rupture releasing orange-brown powdery dust; infected leaves curl and remain attached without falling immediately.",
                "High",
                "Destroy volunteer groundnuts; plant resistant cultivars.",
                "Intercrop with cereal crops to reduce airborne spore transfer.",
                "Apply registered systemic fungicides (e.g. hexaconazole) early upon detection.",
                "Inspect lower leaf surfaces across entire canopy.",
                "Report sudden rapid rust spread in seed production plots.",
                "ICRISAT Technical Bulletin No. 13.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("groundnut_healthy", "groundnuts", "Healthy Groundnut Leaf",
                "N/A", "Healthy",
                "Vigorous groundnut foliage with clear green leaflets, strong vegetative development, and no spots.",
                "Quality seed and well-drained fertile soil.",
                "Tetrafoliate leaves, bright emerald green, smooth margins, no spotting.",
                "Absence of yellow halos, dark spots, or powdery pustules.",
                "Normal",
                "Keep field weed-free; avoid disturbing soil during pegging.",
                "Maintain good drainage.",
                "No treatment required.",
                "Scout weekly.",
                "None needed.",
                "FAO Legume Guide.",
                "2026-01-15", true, "groundnut_healthy", true));

        // BANANA DISEASES
        diseases.add(new Disease("banana_black_sigatoka", "banana", "Black Sigatoka (Black Leaf Streak)",
                "Pseudocercospora fijiensis", "Fungal",
                "The most destructive foliar disease of bananas worldwide, causing extensive leaf destruction.",
                "Airborne ascospores and splash-borne conidia; favored by warm temperatures and continuous high humidity.",
                "Tiny reddish-brown rusty streaks parallel to veins that expand into dark brown to black elliptical spots.",
                "Longitudinal reddish-brown streaks evolving into black sunken spots with gray dried-out centers.",
                "Critical",
                "De-leafing: surgically cut out heavily spotted leaf sections; improve field drainage.",
                "Plant resistant cultivars (FHIA hybrids); increase planting spacing to enhance sunlight and airflow.",
                "Commercial plantations use systematic aerial/ground fungicide rotations. Adhere to resistance management.",
                "Scout third and fourth youngest fully opened leaves for initial streak stages.",
                "Consult agronomy service if fewer than 5 functional leaves remain on flowering plants.",
                "Bioversity International; CABI Black Sigatoka Datasheet.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("banana_bacterial_wilt", "banana", "Banana Bacterial Wilt (BXW)",
                "Xanthomonas vasicola pv. musacearum", "Bacterial",
                "Devastating bacterial disease in East and Central Africa that causes total crop loss and plant death.",
                "Transmitted by insect vectors visiting male flower buds, contaminated cutting tools, and infected suckers.",
                "Yellowing and wilting of leaves, premature uneven fruit ripening with brown staining, yellow bacterial ooze.",
                "Yellow bacterial ooze weeping from cut pseudostem or male bud stalk; internal brown discoloration of fruit pulp.",
                "Critical",
                "Single diseased stem removal (SDSR); sterilize all cutting machetes in fire or bleach.",
                "Debud male flowers using a forked stick (do not cut with machete); use clean tissue culture plantlets.",
                "Chemical pesticides CANNOT cure bacterial wilt. Strict sanitation is the only defense.",
                "Check male floral buds, fruit ripening patterns, and cut pseudostems for yellow ooze. Leaf photo alone is NOT sufficient.",
                "Report immediately to national agricultural authorities and local plant clinic.",
                "IITA BXW Management Guide; FAO Plant Protection.",
                "2026-01-15", false, null, false));

        diseases.add(new Disease("banana_bunchy_top_virus", "banana", "Banana Bunchy Top Virus (BBTV)",
                "Banana bunchy top virus (Nanoviridae)", "Viral",
                "Severe viral disease causing extreme stunting and bunched leaves, rendering plants unproductive.",
                "Transmitted by the banana aphid (Pentalonia nigronervosa) and infected planting suckers.",
                "Narrow, upright, bunched leaves with wavy margins; dark green 'Morse code' dashes on leaf veins.",
                "Distinctive dark green hook-shaped dots and dashes ('Morse code' pattern) along leaf midrib and petiole.",
                "Critical",
                "Kill infected mat with injected herbicide or kerosene; eradicate banana aphids before removal.",
                "Use only certified virus-tested tissue-culture planting material.",
                "No cure exists for virus; spray aphids with approved soap or insecticide before uprooting plants.",
                "Inspect leaf petiole and midrib against the light for Morse code dashes.",
                "Report immediately; subject to strict quarantine enforcement.",
                "Bioversity International BBTV Manual.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("banana_healthy", "banana", "Healthy Banana Leaf",
                "N/A", "Healthy",
                "Vigorous banana leaf with broad expansive green surface, smooth margins, and intact petiole.",
                "Good soil fertility, moisture, and clean planting material.",
                "Broad paddle-shaped blade, bright green with waxy bloom, no dark streaks or yellowing.",
                "No black sigatoka streaks, yellow wilting, or Morse-code veins.",
                "Normal",
                "Desucker to maintain 3 stems per mat (mother, daughter, granddaughter).",
                "Mulch around plants to conserve soil moisture.",
                "No treatment required.",
                "Regular monthly inspection of leaf count.",
                "None needed.",
                "FAO Banana Cultivation Guidelines.",
                "2026-01-15", false, null, true));

        // COFFEE DISEASES
        diseases.add(new Disease("coffee_leaf_rust", "coffee", "Coffee Leaf Rust",
                "Hemileia vastatrix", "Fungal",
                "Major global fungal pathogen of Arabica coffee causing severe defoliation and dieback.",
                "Urediniospores dispersed by wind and rain splash; requires free water for spore germination.",
                "Yellow-orange powdery circular patches on the lower leaf surface with corresponding chlorotic spots above.",
                "Bright orange powdery spore patches on leaf undersides that rub off easily on fingertips.",
                "High to Critical",
                "Prune shade trees to increase light and airflow; maintain optimal plant vigor with balanced fertilization.",
                "Plant resistant varieties (e.g. Ruiru 11, Batian, Castillo); practice timely pruning.",
                "Preventative copper-based sprays applied before rainy season; systemic triazoles if infection passes threshold.",
                "Inspect lower surface of mature leaves in the lower and middle canopy.",
                "Seek agronomy advice if rust covers >10% of leaves on bearing trees.",
                "World Coffee Research; ICO Coffee Disease Guide.",
                "2026-01-15", true, "coffee_leaf_rust", true));

        diseases.add(new Disease("coffee_cercospora_leaf_spot", "coffee", "Cercospora Leaf Spot (Brown Eye Spot)",
                "Cercospora coffeicola", "Fungal",
                "Common foliar and berry disease often triggered by nitrogen deficiency and high sun exposure.",
                "Spreads via airborne and splash-borne spores; exacerbated by stressed or poorly nourished trees.",
                "Circular brown spots with light gray centers surrounded by bright yellow halos ('eye' appearance).",
                "Characteristic 'bird's eye' spots with pale ash-gray centers and concentric rings on leaves and berries.",
                "Moderate",
                "Provide adequate shade in hot regions; ensure balanced nitrogen and potassium fertilization.",
                "Improve soil organic matter; maintain weed-free zones around drip lines.",
                "Foliar copper fungicides applied before flowering; correct nutritional stress.",
                "Check upper leaves and developing green coffee cherries for brown sunken lesions.",
                "Consult extension if berry dropping is observed.",
                "CABI Coffee Guide; CIRAD Coffee Pathology.",
                "2026-01-15", true, "coffee_cercospora_leaf_spot", true));

        diseases.add(new Disease("coffee_berry_disease", "coffee", "Coffee Berry Disease (CBD)",
                "Colletotrichum kahawae", "Fungal",
                "Devastating anthracnose affecting green coffee berries, causing premature mummification and heavy yield loss.",
                "Rain-splashed conidia; thrives at high altitudes with cool, wet weather.",
                "Sunken dark brown to black lesions on expanding green berries; occasionally minor leaf spots.",
                "Dark sunken necrotic lesions directly on green berries causing them to drop or mummify. Leaf photograph alone is NOT diagnostic.",
                "Critical",
                "Prune dead wood and strip mummified berries from previous season.",
                "Plant CBD-resistant Arabica varieties; prune canopy to facilitate drying.",
                "Preventative spray program using copper fungicides and registered strobilurins during berry expansion.",
                "Examine developing green berries on bearing nodes. A leaf photograph alone cannot confirm CBD.",
                "Urgent extension notification required if green berries show black sunken cankers.",
                "Kenya Coffee Research Institute (CRI); CABI CBD Datasheet.",
                "2026-01-15", false, null, false));

        diseases.add(new Disease("coffee_healthy", "coffee", "Healthy Coffee Leaf",
                "N/A", "Healthy",
                "Vigorous, dark green glossy coffee foliage with prominent healthy venation and clean underside.",
                "Optimal soil pH, balanced nutrition, and appropriate shade management.",
                "Shiny dark green, oblong-elliptic leaves with undulating margins and clean surfaces.",
                "No orange powdery pustules, brown eye spots, or necrosis.",
                "Normal",
                "Maintain weed control, mulch, and regular pruning schedule.",
                "Soil testing every two seasons to maintain pH between 5.5 and 6.5.",
                "No treatment required.",
                "Bi-weekly scouting of lower canopy.",
                "None needed.",
                "Specialty Coffee Association Agronomy Manual.",
                "2026-01-15", true, "coffee_healthy", true));

        // RICE DISEASES
        diseases.add(new Disease("rice_blast", "rice", "Rice Blast",
                "Magnaporthe oryzae", "Fungal",
                "One of the most destructive cereal diseases globally, attacking leaves, collars, nodes, and panicles.",
                "Airborne conidia; favored by excessive nitrogen, cool night temperatures, and high humidity (>90%).",
                "Spindle-shaped or diamond-shaped lesions with gray or whitish centers and reddish-brown borders.",
                "Eye-shaped or diamond-shaped lesions tapering at both ends with a distinct ash-gray center.",
                "Critical",
                "Avoid excessive nitrogen fertilization (split nitrogen applications); avoid water stress in paddy.",
                "Plant blast-resistant rice varieties; treat seeds before sowing.",
                "Fungicide sprays (tricyclazole, azoxystrobin) applied at early tillering and panicle emergence.",
                "Examine young leaves during tillering stage and inspect panicle necks at heading.",
                "Notify agricultural extension immediately if neck blast is detected in flowering fields.",
                "IRRI Rice Knowledge Bank; CABI Rice Blast Compendium.",
                "2026-01-15", true, "rice_blast", true));

        diseases.add(new Disease("rice_bacterial_leaf_blight", "rice", "Bacterial Leaf Blight (BLB)",
                "Xanthomonas oryzae pv. oryzae", "Bacterial",
                "Serious bacterial disease causing extensive wilting, yellowing, and drying of leaf blades.",
                "Bacteria enter through wounds or hydathodes; spread through irrigation water, typhoons, and wind-driven rain.",
                "Water-soaked streaks originating from leaf margins, expanding into wavy yellow-white drying lesions.",
                "Wavy, undulating lesion margins running along leaf edges from leaf tips downwards, becoming straw-colored.",
                "High",
                "Drain infected fields briefly to reduce bacterial spread; eliminate weed hosts from bunds.",
                "Plant certified resistant varieties; balanced fertilization without excess nitrogen.",
                "No effective chemical cures exist. Preventative copper sprays have limited efficacy.",
                "Check leaf margins of upper leaves; inspect for small amber bacterial dew drops in early morning.",
                "Report rapidly progressing blights in newly flooded fields.",
                "IRRI BLB Management Guide; FAO Rice Protection.",
                "2026-01-15", true, "rice_bacterial_leaf_blight", true));

        diseases.add(new Disease("rice_brown_spot", "rice", "Brown Spot",
                "Bipolaris oryzae", "Fungal",
                "Chronic foliar disease associated with nutrient-deficient, poorly drained, or drought-stressed soils.",
                "Seed-borne and airborne fungus; prevalent under poor soil fertility conditions.",
                "Small, oval to circular dark brown spots with lighter gray-brown centers, evenly distributed on leaves.",
                "Uniformly distributed small oval spots (like sesame seeds) surrounded by yellow chlorotic halos.",
                "Moderate",
                "Correct soil nutritional deficiencies (apply potassium, silica, and micronutrients).",
                "Hot water or fungicide seed treatment; maintain proper water management in paddies.",
                "Foliar fungicides (mancozeb, propiconazole) if infection appears early in high-value plots.",
                "Scout entire leaf blade of tillering rice.",
                "Refer to soil extension agent to test soil fertility if brown spot is widespread.",
                "IRRI Rice Doctor; CABI Brown Spot Datasheet.",
                "2026-01-15", true, "rice_brown_spot", true));

        diseases.add(new Disease("rice_tungro_disease", "rice", "Rice Tungro Disease",
                "Rice tungro bacilliform virus & spherical virus", "Viral",
                "Viral disease transmitted by green leafhoppers (Nephotettix virescens) causing severe stunting and yellowing.",
                "Vector-transmitted during brief feeding periods; common in irrigated lowland rice.",
                "Leaf discoloration ranging from yellow to bright orange, stunted growth, reduced tillering, delayed flowering.",
                "Vivid yellow-orange leaf discoloration starting from leaf tips; severe plant stunting.",
                "Critical",
                "Rogue infected hills; plow under ratoon rice crops after harvest.",
                "Synchronize planting across irrigation blocks; use tungro-resistant varieties.",
                "Insecticides targeting green leafhoppers at nursery stage. Fungicides have zero effect.",
                "Observe young seedlings and look for green leafhopper presence on stems.",
                "Immediate referral required to prevent regional epidemic.",
                "IRRI Tungro Management Handbook.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("rice_healthy", "rice", "Healthy Rice Leaf",
                "N/A", "Healthy",
                "Vigorous, upright rice leaves with clear light to medium green color, clean veins, and no necrotic spots.",
                "Optimal water management, certified clean seeds, and balanced nutrient management.",
                "Linear erect leaves, vibrant green, smooth margins, clear parallel venation.",
                "No spindle lesions, wavy margin blights, or sesame-like brown spots.",
                "Normal",
                "Maintain optimal floodwater depth and timely topdressing.",
                "Regular weekly field monitoring.",
                "No treatment required.",
                "Weekly scouting.",
                "None needed.",
                "IRRI Standard Evaluation System.",
                "2026-01-15", true, "rice_healthy", true));

        // PEPPER DISEASES
        diseases.add(new Disease("pepper_bacterial_leaf_spot", "pepper", "Bacterial Leaf Spot",
                "Xanthomonas campestris pv. vesicatoria", "Bacterial",
                "Severe bacterial disease in pepper causing defoliation, blossom drop, and unmarketable spotted fruits.",
                "Seed-borne bacteria spread by driving rain, irrigation, and contaminated tools in warm, wet weather.",
                "Small, circular to irregular water-soaked spots turning dark brown, often with pale yellow halos.",
                "Water-soaked underside lesions that turn dark brown with translucent greasy borders; raised fruit warts.",
                "High",
                "Avoid working in fields when foliage is wet; sanitize stakes and pruning knives.",
                "Plant certified pathogen-free, hot-water-treated seed; rotate away from solanaceous crops.",
                "Copper bactericides mixed with mancozeb can suppress spread. Follow pre-harvest intervals.",
                "Examine lower leaf surfaces and green bell/chili peppers.",
                "Consult extension if fruit spots appear in commercial export crop.",
                "University of Florida Extension; WorldVeg Pepper Disease Guide.",
                "2026-01-15", true, "pepper_bacterial_leaf_spot", true));

        diseases.add(new Disease("pepper_cercospora_leaf_spot", "pepper", "Cercospora Leaf Spot (Frogeye)",
                "Cercospora capsici", "Fungal",
                "Foliar fungal disease producing circular lesions that can cause extensive leaf drop in pepper.",
                "Favored by warm temperatures and prolonged leaf wetness.",
                "Circular spots (up to 1 cm) with distinct dark brown margins and white or light gray centers.",
                "Classic 'frogeye' appearance: large circular spot with brilliant white/bleached center and dark ring.",
                "Moderate",
                "Collect and burn crop debris; improve air circulation through wider spacing.",
                "Use drip irrigation; rotate crops for at least two seasons.",
                "Protectant fungicides (chlorothalonil, copper) applied at first sign of frogeye lesions.",
                "Middle and lower canopy foliage and stems.",
                "Refer to extension if leaf shedding threatens fruit with sunscald.",
                "UC Davis Vegetable Research; Plantwise Factsheet.",
                "2026-01-15", true, "pepper_cercospora_leaf_spot", true));

        diseases.add(new Disease("pepper_anthracnose", "pepper", "Anthracnose",
                "Colletotrichum capsici / acutatum", "Fungal",
                "Critical disease primarily targeting pepper fruits with circular sunken lesions, but also causing minor leaf spotting.",
                "Splashing rain and infected seed; thrives in warm humid climates.",
                "Circular sunken lesions on fruit with concentric rings of salmon-pink to black spores; minor leaf spots.",
                "Sunken circular fruit rot with concentric rings of gelatinous salmon-colored spores. Leaf spots are non-distinct.",
                "High",
                "Harvest mature fruits promptly; destroy infected fruit culls immediately.",
                "Use certified disease-free seed; maintain mulch barrier to stop soil rain splash.",
                "Fungicide sprays (strobilurins, copper) starting at first flowering.",
                "Examine developing green and ripe peppers carefully. Leaf examination alone is NOT sufficient.",
                "Refer to extension if fruit rot exceeds 10% in commercial harvest.",
                "WorldVeg Anthracnose Manual; CABI Plantwise.",
                "2026-01-15", false, null, false));

        diseases.add(new Disease("pepper_healthy", "pepper", "Healthy Pepper Leaf",
                "N/A", "Healthy",
                "Glossy, lanceolate green leaves with strong vigor and no necrotic lesions or chlorosis.",
                "Balanced irrigation, weed-free beds, and absence of insect pests.",
                "Smooth, vibrant green leaves with intact margins and clean surfaces.",
                "No bacterial water-soaked spots, frogeye centers, or sunken lesions.",
                "Normal",
                "Maintain consistent watering to prevent blossom end rot.",
                "Scout for thrips and aphids.",
                "No treatment required.",
                "Routine weekly inspection.",
                "None needed.",
                "FAO Vegetable Production Handbook.",
                "2026-01-15", true, "pepper_healthy", true));

        // SORGHUM DISEASES
        diseases.add(new Disease("sorghum_anthracnose", "sorghum", "Anthracnose",
                "Colletotrichum sublineolum", "Fungal",
                "Major disease of sorghum affecting leaves, stalks, and grain heads, causing major yield collapse.",
                "Survives on crop residues and wild sorghum weeds; favored by warm, humid rainy periods.",
                "Circular to elliptical lesions with straw-colored centers and dark red, purple, or brown borders.",
                "Lesions contain black fungal fruiting bodies (acervuli) with hair-like setae visible under hand lens.",
                "High",
                "Rotate with non-host crops like legumes; destroy wild sorghum hosts.",
                "Plant certified resistant sorghum hybrids; bury crop residue.",
                "Fungicide seed dressing; foliar fungicides rarely economical except in seed production.",
                "Check lower and middle leaves and stalk nodes.",
                "Refer to specialist if stalk rot causes lodging.",
                "ICRISAT Sorghum Pathology Guide; CABI Anthracnose Datasheet.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("sorghum_rust", "sorghum", "Sorghum Rust",
                "Puccinia purpurea", "Fungal",
                "Foliar rust disease causing purple to red discoloration and premature leaf death.",
                "Airborne rust spores; thrives in cool, humid conditions.",
                "Small, reddish-purple to brown pustules scattered across both leaf surfaces.",
                "Purple-red colored blisters that rupture to reveal reddish-brown powdery rust spores.",
                "Moderate",
                "Plant rust-resistant cultivars; eradicate alternate weed hosts (Oxalis spp.).",
                "Avoid late planting; maintain balanced soil fertility.",
                "Chemical control generally not recommended for field sorghum.",
                "Scout upper and lower leaf surfaces.",
                "Notify extension if pustules appear before heading on upper canopy.",
                "FAO Cereal Pathology; ICRISAT.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("sorghum_leaf_blight", "sorghum", "Leaf Blight",
                "Exserohilum turcicum", "Fungal",
                "Widespread foliar blight occurring in cool, humid sorghum-growing areas.",
                "Crop residue and airborne spores; similar pathogen to maize northern leaf blight.",
                "Elongated, elliptical lesions with straw-colored centers and red, purple, or brown borders.",
                "Large elliptical lesions (several cm long) with distinctive purple or reddish border pigmentation.",
                "Moderate",
                "Crop rotation with broadleaf crops; deep plowing of infected stover.",
                "Select resistant cultivars; ensure optimal plant spacing.",
                "Seed treatments with registered fungicides.",
                "Inspect lower canopy leaves at vegetative stage.",
                "Consult extension if blighting occurs on seedling stages.",
                "Texas A&M AgriLife Extension; CABI.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("sorghum_healthy", "sorghum", "Healthy Sorghum Leaf",
                "N/A", "Healthy",
                "Robust sorghum leaves with waxy bluish-green coating, clean white midrib, and no spots.",
                "Proper seedbed preparation, adequate soil moisture, and pest-free conditions.",
                "Waxy, broad leaves with strong turgor and clear linear venation.",
                "No purple lesions, reddish pustules, or elliptical blights.",
                "Normal",
                "Maintain weed control during early establishment.",
                "Regular scouting.",
                "No treatment required.",
                "Routine walkthrough.",
                "None needed.",
                "ICRISAT Sorghum Production Guide.",
                "2026-01-15", false, null, true));

        // SWEET POTATO DISEASES
        diseases.add(new Disease("sweet_potato_virus_disease", "sweet_potato", "Sweet Potato Virus Disease (SPVD)",
                "SPFMV + SPCSV complex", "Viral",
                "Synergistic viral complex causing devastating stunting, strap-like leaves, and severe yield collapse.",
                "Transmitted jointly by aphids (SPFMV) and whiteflies (SPCSV); spread via infected vine cuttings.",
                "Severe leaf distortion, puckering, chlorotic mottling, narrow strap-like leaves, and extreme stunting.",
                "Extreme stunting with severely narrow, distorted leaves displaying faint purple ring spots or vein clearing.",
                "Critical",
                "Rogue infected vines immediately and bury them; select clean planting vines from vigorous plants.",
                "Use clean certified virus-tested vine cuttings; isolate new plantings from old diseased fields.",
                "Pesticides cannot cure viruses. Insecticide sprays for vectors are ineffective in open fields.",
                "Inspect terminal vine growth and expanding young leaves.",
                "Notify agricultural extension for certified vine multiplication advice.",
                "CIP SPVD Guide; IITA Plant Health.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("sweet_potato_alternaria_leaf_spot", "sweet_potato", "Alternaria Leaf Spot and Stem Blight",
                "Alternaria bataticola", "Fungal",
                "Foliar and vine disease causing dark necrotic lesions and stem girdling in sweet potato.",
                "Survives on infected vines and soil debris; favored by humid conditions.",
                "Circular brown spots with concentric rings on leaves; black lesions girdling vines causing wilt.",
                "Concentric rings on leaf spots and black sunken lesions on trailing vines.",
                "Moderate to High",
                "Use disease-free vine cuttings; avoid taking cuttings from infected fields.",
                "Destroy infected residues after harvest; rotate with non-host crops.",
                "Foliar fungicides (copper, chlorothalonil) may be applied in severe outbreaks on vine nurseries.",
                "Inspect leaf blades and examine trailing stems for black girdling lesions.",
                "Consult extension if vines wilt suddenly without root rot.",
                "CIP Pathology Manual; Plantwise Factsheet.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("sweet_potato_bacterial_leaf_spot", "sweet_potato", "Bacterial Leaf Spot",
                "Xanthomonas campestris", "Bacterial",
                "Bacterial infection causing angular water-soaked leaf spots on sweet potato vines.",
                "Splashing rain and infected planting material; favored by warm temperatures.",
                "Small angular water-soaked spots bounded by veins, later turning dark brown with chlorotic halos.",
                "Angular, vein-bounded water-soaked spots that tear easily in windy conditions.",
                "Moderate",
                "Ensure vine cuttings are sourced from disease-free mother gardens.",
                "Do not handle or cultivate fields while foliage is wet.",
                "Preventative copper sprays can assist in reducing bacterial spread in nursery beds.",
                "Examine lower and middle foliage for water-soaked angular spots.",
                "Seek confirmation if widespread blighting occurs in vine multiplication beds.",
                "FAO Sweet Potato Guidelines.",
                "2026-01-15", false, null, true));

        diseases.add(new Disease("sweet_potato_healthy", "sweet_potato", "Healthy Sweet Potato Leaf",
                "N/A", "Healthy",
                "Lush, vigorous heart-shaped or lobed leaves with vibrant green color and supple trailing vines.",
                "Clean vine cuttings, good soil moisture, and adequate soil potassium.",
                "Heart-shaped or palmate lobes, deep green, supple texture, clean veins.",
                "No leaf narrowing, purple spotting, mosaic puckering, or angular water-soaking.",
                "Normal",
                "Maintain weed control until vine canopy closes.",
                "Hill soil around root zone to prevent sweet potato weevil entry.",
                "No treatment required.",
                "Routine scouting.",
                "None needed.",
                "CIP Sweet Potato Production Manual.",
                "2026-01-15", false, null, true));

        db.diseaseDao().insertDiseases(diseases);

        // 3. Seed Differential Features (for pairwise comparisons)
        List<DifferentialFeature> features = new ArrayList<>();
        // Tomato differentials: Early Blight vs Septoria vs Bacterial Spot
        features.add(new DifferentialFeature("tomato", "tomato_early_blight", "tomato_septoria_leaf_spot",
                "Spot Size & Concentric Rings",
                "Early blight spots are larger (5-15 mm) with prominent concentric target rings, whereas Septoria spots are small (1.5-3 mm) with pale centers and tiny black dots (pycnidia).",
                "Lower leaves", "Look closely with a magnifying glass for tiny pepper-like black specks inside Septoria spots."));

        features.add(new DifferentialFeature("tomato", "tomato_early_blight", "tomato_bacterial_spot",
                "Spot Geometry & Water-Soaking",
                "Early blight produces target rings on older foliage. Bacterial spot produces smaller, angular water-soaked spots that often tear and shot-hole.",
                "Leaves and developing green fruit", "Check fruits: bacterial spot creates raised scabby blisters on green fruit, whereas early blight creates leathery black stem-end fruit rot."));

        features.add(new DifferentialFeature("tomato", "tomato_septoria_leaf_spot", "tomato_bacterial_spot",
                "Fruiting Bodies vs Shot-hole Tearing",
                "Septoria spots have distinct light-gray centers containing black fruiting bodies. Bacterial spot lesions are greasy, angular, lack black specks, and often tear the leaf tissue.",
                "Underside of leaves", "Observe spot borders: bacterial spot has irregular translucent halos without internal pycnidia."));

        // Maize differentials: Northern Leaf Blight vs Gray Leaf Spot vs Common Rust
        features.add(new DifferentialFeature("maize", "maize_northern_leaf_blight", "maize_gray_leaf_spot",
                "Lesion Shape & Margins",
                "Northern leaf blight forms elongated cigar-shaped lesions (up to 15 cm) with tapered ends. Gray leaf spot forms strictly rectangular lesions bounded squarely between veins.",
                "Middle canopy leaves", "Check if lesions have curved tapered edges (Northern Leaf Blight) or straight parallel rectangular edges (Gray Leaf Spot)."));

        features.add(new DifferentialFeature("maize", "maize_common_rust", "maize_gray_leaf_spot",
                "Pustules vs Flat Necrosis",
                "Common rust forms raised powdery cinnamon-brown pustules that rub off on fingers. Gray leaf spot consists of flat rectangular dry necrotic spots that do not rub off.",
                "Upper and lower leaf surfaces", "Wipe finger across the spot: rust leaves a powdery red-brown smudge; leaf spot does not."));

        // Cassava differentials: CMD vs CBSD
        features.add(new DifferentialFeature("cassava", "cassava_mosaic_disease", "cassava_brown_streak_disease",
                "Leaf Distortion vs Vein Chlorosis & Root Rot",
                "CMD causes severe leaf distortion, puckering, and asymmetrical stunted lobes. CBSD causes feathery yellowing along veins on older leaves without puckering, but causes brown corky root rot.",
                "New leaves for CMD; older leaves, stems, and tuberous roots for CBSD",
                "Leaf photograph alone CANNOT confirm CBSD! Inspect tuber roots for internal brown dry rot."));

        // Coffee differentials: Leaf Rust vs Cercospora
        features.add(new DifferentialFeature("coffee", "coffee_leaf_rust", "coffee_cercospora_leaf_spot",
                "Powdery Underside vs Bird's Eye Spot",
                "Coffee leaf rust forms distinct powdery orange patches on the underside of leaves. Cercospora forms discrete brown 'bird's eye' spots with pale ash centers and yellow halos.",
                "Underside of lower leaves", "Look underneath: bright orange powder indicates rust; smooth brown spot with ash center indicates Cercospora."));

        // Rice differentials: Blast vs Bacterial Leaf Blight vs Brown Spot
        features.add(new DifferentialFeature("rice", "rice_blast", "rice_bacterial_leaf_blight",
                "Spindle Lesions vs Margin Blight",
                "Rice blast produces diamond- or spindle-shaped lesions across the blade with gray centers. Bacterial leaf blight starts at leaf tips/margins creating wavy yellow-white drying bands down the edges.",
                "Leaf blade vs leaf margins", "Check whether lesions are isolated diamond spots or continuous margin-drying bands."));

        features.add(new DifferentialFeature("rice", "rice_blast", "rice_brown_spot",
                "Lesion Geometry & Size",
                "Blast lesions are spindle-shaped with pointed ends and gray centers. Brown spot lesions are smaller, oval, sesame-seed-like with brown centers and yellow halos.",
                "Entire leaf blade", "Look at lesion ends: pointed spindle indicates blast; rounded oval indicates brown spot."));

        db.differentialFeatureDao().insertFeatures(features);

        // 4. Seed Symptom Questions & Effects (Follow-up Checklists)
        List<SymptomQuestion> questions = new ArrayList<>();
        List<SymptomAnswerEffect> effects = new ArrayList<>();

        // Tomato Checklist
        questions.add(new SymptomQuestion("tom_q_concentric_rings", "tomato",
                "Do larger brown spots have concentric, target-like rings?",
                "Concentric rings look like an archery target and are characteristic of Early Blight.",
                "tomato_early_blight", 1));
        effects.add(new SymptomAnswerEffect("tom_q_concentric_rings", "tomato_early_blight", "YES", 3,
                "Target-like concentric rings strongly support Early Blight."));
        effects.add(new SymptomAnswerEffect("tom_q_concentric_rings", "tomato_septoria_leaf_spot", "YES", -1,
                "Target rings are atypical for Septoria leaf spot."));
        effects.add(new SymptomAnswerEffect("tom_q_concentric_rings", "tomato_bacterial_spot", "YES", -2,
                "Bacterial spot does not produce concentric target rings."));

        questions.add(new SymptomQuestion("tom_q_pale_centers_black_dots", "tomato",
                "Are there numerous small, circular spots with pale or gray centres containing tiny black specks?",
                "Tiny specks are fungal pycnidia fruiting bodies, highly characteristic of Septoria.",
                "tomato_septoria_leaf_spot", 2));
        effects.add(new SymptomAnswerEffect("tom_q_pale_centers_black_dots", "tomato_septoria_leaf_spot", "YES", 3,
                "Pale centers with tiny black fruiting specks strongly support Septoria leaf spot."));
        effects.add(new SymptomAnswerEffect("tom_q_pale_centers_black_dots", "tomato_early_blight", "YES", -1,
                "Early blight lesions are larger and lack tiny internal pycnidia specks."));

        questions.add(new SymptomQuestion("tom_q_angular_water_soaked", "tomato",
                "Are lesions angular, limited by leaf veins, or appear water-soaked/greasy?",
                "Angular margins conforming to veins suggest bacterial infection rather than fungal blights.",
                "tomato_bacterial_spot", 3));
        effects.add(new SymptomAnswerEffect("tom_q_angular_water_soaked", "tomato_bacterial_spot", "YES", 3,
                "Angular, water-soaked lesions are consistent with Bacterial Spot."));
        effects.add(new SymptomAnswerEffect("tom_q_angular_water_soaked", "tomato_early_blight", "YES", -1,
                "Early blight typically produces rounded, expanding spots rather than angular vein-limited lesions."));

        questions.add(new SymptomQuestion("tom_q_white_mold_underside", "tomato",
                "Is there a white, downy cottony mold visible on the underside of infected leaves in damp conditions?",
                "White sporulation on the underside of dark water-soaked lesions is the hallmark of Late Blight.",
                "tomato_late_blight", 4));
        effects.add(new SymptomAnswerEffect("tom_q_white_mold_underside", "tomato_late_blight", "YES", 4,
                "White downy sporulation on the leaf underside strongly indicates active Late Blight."));
        effects.add(new SymptomAnswerEffect("tom_q_white_mold_underside", "tomato_early_blight", "YES", -2,
                "Early blight does not produce white downy mold on leaf undersides."));

        questions.add(new SymptomQuestion("tom_q_older_leaves_first", "tomato",
                "Are symptoms heavily concentrated on the lower, older leaves near the soil?",
                "Early blight and Septoria typically begin on the lowest foliage, while Late Blight can strike any canopy level.",
                "tomato_early_blight", 5));
        effects.add(new SymptomAnswerEffect("tom_q_older_leaves_first", "tomato_early_blight", "YES", 1,
                "Progression starting on lower leaves aligns with Early Blight and Septoria."));
        effects.add(new SymptomAnswerEffect("tom_q_older_leaves_first", "tomato_late_blight", "NO", 1,
                "Late blight can initiate anywhere in the canopy following airborne spore deposition."));

        // Maize Checklist
        questions.add(new SymptomQuestion("maize_q_cigar_shaped", "maize",
                "Are there long, cigar-shaped gray-green or tan lesions (2.5 to 15 cm)?",
                "Spindle or cigar-shaped lesions are the classic indicator of Northern Leaf Blight.",
                "maize_northern_leaf_blight", 1));
        effects.add(new SymptomAnswerEffect("maize_q_cigar_shaped", "maize_northern_leaf_blight", "YES", 3,
                "Long cigar-shaped lesions strongly support Northern Leaf Blight."));
        effects.add(new SymptomAnswerEffect("maize_q_cigar_shaped", "maize_gray_leaf_spot", "YES", -2,
                "Gray leaf spot creates strictly rectangular spots, not cigar-shaped spindles."));

        questions.add(new SymptomQuestion("maize_q_rectangular_veins", "maize",
                "Are lesions strictly rectangular, parallel to veins with sharp square boundaries?",
                "Vein-delimited rectangular geometry is diagnostic for Gray Leaf Spot.",
                "maize_gray_leaf_spot", 2));
        effects.add(new SymptomAnswerEffect("maize_q_rectangular_veins", "maize_gray_leaf_spot", "YES", 3,
                "Strict rectangular geometry between veins strongly supports Gray Leaf Spot."));
        effects.add(new SymptomAnswerEffect("maize_q_rectangular_veins", "maize_northern_leaf_blight", "YES", -2,
                "Northern leaf blight lesions have curved tapered edges rather than square rectangular boundaries."));

        questions.add(new SymptomQuestion("maize_q_raised_pustules", "maize",
                "Are raised reddish-brown or orange powdery pustules present on both leaf surfaces?",
                "Pustules that release powder when rubbed confirm Common Rust.",
                "maize_common_rust", 3));
        effects.add(new SymptomAnswerEffect("maize_q_raised_pustules", "maize_common_rust", "YES", 4,
                "Raised powdery pustules confirm Common Rust."));
        effects.add(new SymptomAnswerEffect("maize_q_raised_pustules", "maize_gray_leaf_spot", "YES", -3,
                "Gray leaf spot does not produce powdery pustules."));

        // Cassava Checklist
        questions.add(new SymptomQuestion("cass_q_mosaic_distortion", "cassava",
                "Is there a yellow-and-green mosaic pattern with distorted, twisted, or asymmetrical leaflets?",
                "Leaf distortion and mosaic mottling are classic signs of Cassava Mosaic Disease.",
                "cassava_mosaic_disease", 1));
        effects.add(new SymptomAnswerEffect("cass_q_mosaic_distortion", "cassava_mosaic_disease", "YES", 3,
                "Puckered mosaic with distorted leaflets strongly supports Cassava Mosaic Disease."));
        effects.add(new SymptomAnswerEffect("cass_q_mosaic_distortion", "cassava_brown_streak_disease", "YES", -1,
                "CBSD typically does not cause severe leaf curling or puckered asymmetry."));

        questions.add(new SymptomQuestion("cass_q_feathery_vein_yellowing", "cassava",
                "Is there feathery yellowing along secondary veins without leaf curling?",
                "Feathery chlorosis on older leaves without distortion suggests Cassava Brown Streak Disease.",
                "cassava_brown_streak_disease", 2));
        effects.add(new SymptomAnswerEffect("cass_q_feathery_vein_yellowing", "cassava_brown_streak_disease", "YES", 3,
                "Feathery vein chlorosis supports Cassava Brown Streak Disease."));

        questions.add(new SymptomQuestion("cass_q_root_brown_rot", "cassava",
                "Are roots showing dry brown internal corky discoloration upon slicing?",
                "IMPORTANT: Root symptoms cannot be evaluated from leaf images alone.",
                "cassava_brown_streak_disease", 3));
        effects.add(new SymptomAnswerEffect("cass_q_root_brown_rot", "cassava_brown_streak_disease", "YES", 4,
                "Internal dry corky root rot confirms Cassava Brown Streak Disease (CBSD)."));

        // Coffee Checklist
        questions.add(new SymptomQuestion("cof_q_orange_powder_underside", "coffee",
                "Are bright orange or yellow-orange powdery patches visible on the underside of leaves?",
                "Powdery orange spores on leaf undersides are the hallmark of Coffee Leaf Rust.",
                "coffee_leaf_rust", 1));
        effects.add(new SymptomAnswerEffect("cof_q_orange_powder_underside", "coffee_leaf_rust", "YES", 4,
                "Orange powdery patches on the leaf underside confirm Coffee Leaf Rust."));
        effects.add(new SymptomAnswerEffect("cof_q_orange_powder_underside", "coffee_cercospora_leaf_spot", "YES", -3,
                "Cercospora does not form powdery orange patches on leaf undersides."));

        questions.add(new SymptomQuestion("cof_q_birds_eye_spots", "coffee",
                "Are there circular brown spots with lighter ash-gray centers and yellow halos ('bird's eye')?",
                "Distinctive bird's eye pattern indicates Cercospora Leaf Spot.",
                "coffee_cercospora_leaf_spot", 2));
        effects.add(new SymptomAnswerEffect("cof_q_birds_eye_spots", "coffee_cercospora_leaf_spot", "YES", 3,
                "Bird's eye spot pattern strongly supports Cercospora Leaf Spot."));

        // Rice Checklist
        questions.add(new SymptomQuestion("rice_q_diamond_spindle", "rice",
                "Are lesions diamond- or spindle-shaped with pale ash centers and reddish-brown borders?",
                "Spindle shape tapering at both ends is the classic hallmark of Rice Blast.",
                "rice_blast", 1));
        effects.add(new SymptomAnswerEffect("rice_q_diamond_spindle", "rice_blast", "YES", 3,
                "Spindle-shaped lesions with ash centers strongly support Rice Blast."));
        effects.add(new SymptomAnswerEffect("rice_q_diamond_spindle", "rice_bacterial_leaf_blight", "YES", -2,
                "Bacterial leaf blight causes wavy margin drying rather than spindle lesions."));

        questions.add(new SymptomQuestion("rice_q_wavy_margin_drying", "rice",
                "Are yellowing or drying lesions extending down the leaf tips or along leaf edges with wavy margins?",
                "Wavy margin blighting from leaf tips downward indicates Bacterial Leaf Blight.",
                "rice_bacterial_leaf_blight", 2));
        effects.add(new SymptomAnswerEffect("rice_q_wavy_margin_drying", "rice_bacterial_leaf_blight", "YES", 3,
                "Wavy margin lesions extending down leaf edges strongly support Bacterial Leaf Blight."));

        questions.add(new SymptomQuestion("rice_q_sesame_oval_spots", "rice",
                "Are spots small, oval (like sesame seeds), dark brown, and evenly scattered across the blade?",
                "Small uniform sesame-seed-like spots indicate Brown Spot.",
                "rice_brown_spot", 3));
        effects.add(new SymptomAnswerEffect("rice_q_sesame_oval_spots", "rice_brown_spot", "YES", 3,
                "Small oval sesame-seed-like spots support Brown Spot."));

        db.symptomQuestionDao().insertQuestions(questions);
        db.symptomQuestionDao().insertAnswerEffects(effects);

        // 5. Seed Model Classes
        List<ModelClass> modelClasses = new ArrayList<>();
        // Supported classes with calibrated thresholds (0.0 to 1.0)
        modelClasses.add(new ModelClass("tomato_early_blight", "tomato", "tomato_early_blight", 0.35f, false));
        modelClasses.add(new ModelClass("tomato_late_blight", "tomato", "tomato_late_blight", 0.35f, false));
        modelClasses.add(new ModelClass("tomato_septoria_leaf_spot", "tomato", "tomato_septoria_leaf_spot", 0.35f, false));
        modelClasses.add(new ModelClass("tomato_bacterial_spot", "tomato", "tomato_bacterial_spot", 0.35f, false));
        modelClasses.add(new ModelClass("tomato_healthy", "tomato", "tomato_healthy", 0.40f, true));

        modelClasses.add(new ModelClass("maize_northern_leaf_blight", "maize", "maize_northern_leaf_blight", 0.35f, false));
        modelClasses.add(new ModelClass("maize_common_rust", "maize", "maize_common_rust", 0.35f, false));
        modelClasses.add(new ModelClass("maize_gray_leaf_spot", "maize", "maize_gray_leaf_spot", 0.35f, false));
        modelClasses.add(new ModelClass("maize_healthy", "maize", "maize_healthy", 0.40f, true));

        modelClasses.add(new ModelClass("potato_early_blight", "potato", "potato_early_blight", 0.35f, false));
        modelClasses.add(new ModelClass("potato_late_blight", "potato", "potato_late_blight", 0.35f, false));
        modelClasses.add(new ModelClass("potato_healthy", "potato", "potato_healthy", 0.40f, true));

        modelClasses.add(new ModelClass("cassava_mosaic_disease", "cassava", "cassava_mosaic_disease", 0.35f, false));
        modelClasses.add(new ModelClass("cassava_bacterial_blight", "cassava", "cassava_bacterial_blight", 0.35f, false));
        modelClasses.add(new ModelClass("cassava_healthy", "cassava", "cassava_healthy", 0.40f, true));

        modelClasses.add(new ModelClass("coffee_leaf_rust", "coffee", "coffee_leaf_rust", 0.35f, false));
        modelClasses.add(new ModelClass("coffee_cercospora_leaf_spot", "coffee", "coffee_cercospora_leaf_spot", 0.35f, false));
        modelClasses.add(new ModelClass("coffee_healthy", "coffee", "coffee_healthy", 0.40f, true));

        modelClasses.add(new ModelClass("rice_blast", "rice", "rice_blast", 0.35f, false));
        modelClasses.add(new ModelClass("rice_bacterial_leaf_blight", "rice", "rice_bacterial_leaf_blight", 0.35f, false));
        modelClasses.add(new ModelClass("rice_brown_spot", "rice", "rice_brown_spot", 0.35f, false));
        modelClasses.add(new ModelClass("rice_healthy", "rice", "rice_healthy", 0.40f, true));

        modelClasses.add(new ModelClass("pepper_bacterial_leaf_spot", "pepper", "pepper_bacterial_leaf_spot", 0.35f, false));
        modelClasses.add(new ModelClass("pepper_cercospora_leaf_spot", "pepper", "pepper_cercospora_leaf_spot", 0.35f, false));
        modelClasses.add(new ModelClass("pepper_healthy", "pepper", "pepper_healthy", 0.40f, true));

        modelClasses.add(new ModelClass("groundnut_early_leaf_spot", "groundnuts", "groundnut_early_leaf_spot", 0.35f, false));
        modelClasses.add(new ModelClass("groundnut_late_leaf_spot", "groundnuts", "groundnut_late_leaf_spot", 0.35f, false));
        modelClasses.add(new ModelClass("groundnut_healthy", "groundnuts", "groundnut_healthy", 0.40f, true));

        modelClasses.add(new ModelClass("bean_angular_leaf_spot", "beans", "bean_angular_leaf_spot", 0.35f, false));
        modelClasses.add(new ModelClass("bean_rust", "beans", "bean_rust", 0.35f, false));
        modelClasses.add(new ModelClass("bean_anthracnose", "beans", "bean_anthracnose", 0.35f, false));
        modelClasses.add(new ModelClass("bean_healthy", "beans", "bean_healthy", 0.40f, true));

        db.diseaseDao().insertModelClasses(modelClasses);

        // 6. Seed Recommended Inspections with specific intervals
        List<RecommendedInspection> inspections = new ArrayList<>();
        inspections.add(new RecommendedInspection("tomato_late_blight", "Leaf Underside & Stem Check",
                "Lower and middle canopy, leaf undersides", 3,
                "Check every 3 days during wet or foggy weather. Look for expanding dark water-soaked spots with white downy mold underneath.",
                "If >5% of plants show greasy black stem lesions, consult agricultural extension immediately."));

        inspections.add(new RecommendedInspection("tomato_early_blight", "Lower Foliage & Pruning Review",
                "Lowest 30 cm of foliage", 7,
                "Inspect lower leaves weekly for expanding target-like concentric rings. Remove lowest yellowing leaves.",
                "If target spots reach middle canopy before first fruit cluster ripens, consider protective fungicide."));

        inspections.add(new RecommendedInspection("maize_northern_leaf_blight", "Ear Leaf Scouting",
                "Leaves at and above primary ear", 7,
                "Inspect leaves around the ear 2 weeks before and after tasseling. Note lesion length and count.",
                "If lesions exceed 5 cm on ear leaf across >20% of plants before dough stage, seek specialist advice."));

        inspections.add(new RecommendedInspection("cassava_brown_streak_disease", "Stem & Root Cross-Section Inspection",
                "Stems and tuberous storage roots", 14,
                "Carefully dig and sample 2-3 roots per field quadrant; slice transversely to inspect for dry brown internal rot.",
                "Any internal brown corky rot warrants reporting to the district quarantine officer."));

        inspections.add(new RecommendedInspection("coffee_leaf_rust", "Lower Canopy Underside Audit",
                "Lower branches, underside of mature leaves", 14,
                "Check 20 trees randomly across the block. Turn leaves over and count leaves with active orange powder.",
                "If more than 5% of surveyed leaves show active orange sporulation before rainy season, initiate spray program."));

        inspections.add(new RecommendedInspection("rice_blast", "Neck and Collar Inspection",
                "Panicle neck and flag leaf collar", 5,
                "Inspect leaf blast at maximum tillering, and check panicle necks carefully at 50% heading.",
                "Blackened rotting panicle necks (neck blast) cause total whitehead sterility; alert local agronomist."));

        db.diseaseDao().insertRecommendedInspections(inspections);

        Log.d(TAG, "Database successfully seeded with 12 crops and full disease catalogue!");
    }
}

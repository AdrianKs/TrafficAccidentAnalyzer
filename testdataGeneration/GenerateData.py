import csv
import random
import json


# Timestamp
# Lat
# Long
# Marke
# Model
# Baujahr
# Farbe
# Anzahl Insassen
# Airbag links vorne
# Airbag rechts vorn
# Airbag links mitte
# Airbag rechts mitte
# Reifen links vorne
# Reifen rechts vorne
# Reifen links hinten
# Reifen rechts hinten
# Frontscheibe
# Scheibe links vorne
# Scheibe rechts vorne
# Scheibe links hinten
# Scheibe rechts hinten
# Heckscheibe
# Geschwindigkeit vor 10 Sekunden
# Geschwindigkeit vor 5 Sekunden
# Geschwindigkeit vor 3 Sekunden
# Geschwindigkeit vor 2 Sekunden
# Geschwindigkeit vor 1 Sekunden

############################
###Functions and Datasets###
############################

def truncate(f, n):
    # Truncates/pads a float f to n decimal places without rounding
    s = '{}'.format(f)
    if 'e' in s or 'E' in s:
        return float('{0:.{1}f}'.format(f, n))
    i, p, d = s.partition('.')
    return float('.'.join([i, (d + '0' * n)[:n]]))


def appendDecimals(number, appendix):
    if appendix < 10:
        return float(str(number) + "0" + str(appendix))
    else:
        return float(str(number) + str(appendix))


# Read all accident locations from json
with open('accLocations.json') as data_file:
    accLocations = json.load(data_file)

# Read all brands from json
with open('brands.json') as data_file:
    brands = json.load(data_file)

# Read all models from json
with open('models.json') as data_file:
    models = json.load(data_file)

# Read all colors from json
with open('colors.json') as data_file:
    colors = json.load(data_file)

weightedAccLocationsArray = []
for accLocation in accLocations:
    for i in range(accLocation["p"]):
        weightedAccLocationsArray.append(accLocation)


def generateNewLatOrLong(latOrLong):
    newLatOrLong = truncate(latOrLong, 4)
    randomInt = random.randint(0, 99)
    newLatOrLong = appendDecimals(newLatOrLong, randomInt)
    return newLatOrLong


def getLatAndLong():
    i = random.randint(0, (len(weightedAccLocationsArray) - 1))
    latitude = weightedAccLocationsArray[i]["latitude"]
    latitude = generateNewLatOrLong(latitude)
    longitude = weightedAccLocationsArray[i]["longitude"]
    longitude = generateNewLatOrLong(longitude)
    return [latitude, longitude]


weightedBrandArray = []
for brand in brands:
    for i in range(brand["p"]):
        weightedBrandArray.append(brand["name"])


def getBrand():
    i = random.randint(0, (len(weightedBrandArray) - 1))
    return weightedBrandArray[i]


def getModel(brandName):
    modelArray = models[brandName]
    i = random.randint(0, (len(modelArray) - 1))
    return modelArray[i]


weightedColorArray = []
for color in colors:
    for i in range(color["p"]):
        weightedColorArray.append(color["name"])


def getColor():
    i = random.randint(0, (len(weightedColorArray) - 1))
    return weightedColorArray[i]


def getTiresOrWindowsDamaged(v1SecAgo, v2SecAgo, v3SecAgo, abLeftFront, abRightFront, abLeftMid, abRightMid):
    if ((v1SecAgo + v2SecAgo + v3SecAgo) / 3) > 85:
        if abLeftFront:
            if random.randint(1, 10) > 4:
                leftFront = True
            else:
                leftFront = False
        else:
            if random.randint(1, 10) > 7:
                leftFront = True
            else:
                leftFront = False

        if abRightFront:
            if random.randint(1, 10) > 4:
                rightFront = True
            else:
                rightFront = False
        else:
            if random.randint(1, 10) > 7:
                rightFront = True
            else:
                rightFront = False

        if abLeftMid:
            if random.randint(1, 10) > 4:
                leftRear = True
            else:
                leftRear = False
        else:
            if random.randint(1, 10) > 7:
                leftRear = True
            else:
                leftRear = False

        if abRightMid:
            if random.randint(1, 10) > 4:
                rightRear = True
            else:
                rightRear = False
        else:
            if random.randint(1, 10) > 7:
                rightRear = True
            else:
                rightRear = False
    else:
        if abLeftFront:
            if random.randint(1, 10) > 9:
                leftFront = True
            else:
                leftFront = False
        else:
            if random.randint(1, 30) > 29:
                leftFront = True
            else:
                leftFront = False

        if abRightFront:
            if random.randint(1, 10) > 9:
                rightFront = True
            else:
                rightFront = False
        else:
            if random.randint(1, 30) > 29:
                rightFront = True
            else:
                rightFront = False

        if abLeftMid:
            if random.randint(1, 10) > 9:
                leftRear = True
            else:
                leftRear = False
        else:
            if random.randint(1, 30) > 29:
                leftRear = True
            else:
                leftRear = False

        if abRightMid:
            if random.randint(1, 10) > 9:
                rightRear = True
            else:
                rightRear = False
        else:
            if random.randint(1, 30) > 29:
                rightRear = True
            else:
                rightRear = False

    return [leftFront, rightFront, leftRear, rightRear]


def frontOrRearWindowCrashed(abLeft, abRight):
    if abLeft and abRight:
        if random.randint(1, 10) > 4:
            window = True
        else:
            window = False
    elif (abLeft and not (abRight)) or (not (abLeft) and abRight):
        if random.randint(1, 10) > 7:
            window = True
        else:
            window = False
    else:
        if random.randint(1, 30) > 29:
            window = True
        else:
            window = False

    return window


#######################
###Generate Datasets###
#######################
timestamp = 0
headerRow = ["timestamp", "lat", "long", "brand", "model", "constrYear", "color", "passengers", "abLeftFront",
             "abRightFront",
             "abLeftMid", "abRightMid", "tireLeftFront", "tireRightFront", "tireLeftRear", "tireRightRear",
             "windowFront",
             "windowLeftFront", "windowRightFront", "windowLeftRear", "windowRightRear", "windowRear", "v10SecAgo",
             "v5SecAgo", "v3SecAgo", "v2SecAgo", "v1SecAgo"]

with open('../src/main/resources/taa.csv', 'wb') as csvfile:
    spamwriter = csv.writer(csvfile, delimiter=',',
                            quotechar='\'', quoting=csv.QUOTE_MINIMAL)
    spamwriter.writerow(headerRow)

    for i in range(100000):
        timestamp = timestamp + random.randint(1, 10)
        latAndLong = getLatAndLong()
        latitude = latAndLong[0]
        longitude = latAndLong[1]
        brand = getBrand()
        model = getModel(brand)

        p = random.randint(1,100);
        if p <= 20:
            constrYear = random.randint(1995, 1996)
        elif p > 20 and p <= 40:
            constrYear = random.randint(1997, 1999)
        elif p > 40 and p <= 60:
            constrYear = random.randint(2000, 2003)
        elif p > 60 and p <= 85:
            constrYear = random.randint(2004, 2011)
        else:
            constrYear = random.randint(2012, 2017)

        color = getColor()
        passengers = random.randint(1, 5)

        v10SecAgo = random.randint(0, 220)
        v5SecAgo = random.randint(int(v10SecAgo * 0.5), int(v10SecAgo * 1.3))
        v3SecAgo = random.randint(int(v5SecAgo * 0.5), int(v5SecAgo * 1.3))
        v2SecAgo = random.randint(int(v3SecAgo * 0.4), int(v3SecAgo * 1.2))
        v1SecAgo = random.randint(int(v2SecAgo * 0.3), int(v2SecAgo * 1.1))

        # Check if airbags were activated
        if v1SecAgo > 40:
            if random.randint(1, 10) > 1:
                abLeftFront = True
            else:
                abLeftFront = False

            if random.randint(1, 10) > 1:
                abRightFront = True
            else:
                abRightFront = False

            if random.randint(1, 10) > 1:
                abLeftMid = True
            else:
                abLeftMid = False

            if random.randint(1, 10) > 1:
                abRightMid = True
            else:
                abRightMid = False
        else:
            abLeftFront = bool(random.getrandbits(1))
            abRightFront = bool(random.getrandbits(1))
            abLeftMid = bool(random.getrandbits(1))
            abRightMid = bool(random.getrandbits(1))

        tiresDamaged = getTiresOrWindowsDamaged(v1SecAgo, v2SecAgo, v3SecAgo, abLeftFront, abRightFront, abLeftMid,
                                                abRightMid)
        tireLeftFront = tiresDamaged[0]
        tireRightFront = tiresDamaged[1]
        tireLeftRear = tiresDamaged[2]
        tireRightRear = tiresDamaged[3]

        windowsDamaged = getTiresOrWindowsDamaged(v1SecAgo, v2SecAgo, v3SecAgo, abLeftFront, abRightFront, abLeftMid,
                                                  abRightMid)
        windowLeftFront = windowsDamaged[0]
        windowRightFront = windowsDamaged[1]
        windowLeftRear = windowsDamaged[2]
        windowRightRear = windowsDamaged[3]

        windowFront = frontOrRearWindowCrashed(abLeftFront, abRightFront)
        windowRear = frontOrRearWindowCrashed(abLeftMid, abRightMid)

        accident = [timestamp, latitude, longitude, brand, model, constrYear, color, passengers, abLeftFront,
                    abRightFront,
                    abLeftMid, abRightMid, tireLeftFront, tireRightFront, tireLeftRear, tireRightRear, windowFront,
                    windowLeftFront, windowRightFront, windowLeftRear, windowRightRear, windowRear, v10SecAgo, v5SecAgo,
                    v3SecAgo, v2SecAgo, v1SecAgo]
        spamwriter.writerow(accident)
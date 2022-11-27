import java.io.File

fun main(args: Array<String>) {

    val resalePlan = File("src/main/resources/resale-plans.csv").readLines() as MutableList
    resalePlan.removeAt(0)
    var resaleList = mutableListOf<MutableList<String>>()
    resalePlan.forEach { line ->
        resaleList.add(line.split(",") as MutableList<String>)
    }

    val carrierPlan = File("src/main/resources/carrier-plans.csv").readLines() as MutableList
    carrierPlan.removeAt(0)
    var carrierList = mutableListOf<MutableList<String>>()
    carrierPlan.forEach { line ->
        carrierList.add(line.split(",") as MutableList<String>)
    }

    resaleList = resaleList.filter { it.size > 1 } as MutableList<MutableList<String>>

    carrierList = carrierList.filter { it.size > 1 } as MutableList<MutableList<String>>

    var lteSoc = ""
    val listJoinedByMDN = mutableListOf<MutableList<String>>()
    resaleList.forEach { resale ->
        carrierList.forEach { carrier ->
            if (resale[0] == carrier[1].replace(oldValue = "\"", newValue = "")) {
                if (carrier[3].contains("DSMLTESOC")) {
                    lteSoc = "Y"
                } else {
                    lteSoc = "N"
                }
                listJoinedByMDN.add((resale + carrier + mutableListOf(lteSoc)) as MutableList<String>)
            }
        }
    }

    val resalePlanMap = mutableMapOf<String, Int>()
    resaleList.forEach { resale ->
        if (resalePlanMap.contains(resale[1])) {
            resalePlanMap[resale[1]] = resalePlanMap[resale[1]] as Int + 1
        } else {
            resalePlanMap[resale[1]] = 1
        }
    }

    File("Result_1_Kresimir_Hajduk.csv").printWriter().use { out ->
        out.println("MDN,Resale Plan,Sprint Plan,SOCs")
        listJoinedByMDN.forEach { line ->
            out.println(line[0] + "," + line[1] + "," + line[4] + "," + line[5])
        }
    }

    File("Result_2_Kresimir_Hajduk.csv").printWriter().use { out ->
        out.println("MDN,Resale Plan,Sprint Plan,LTE SOC")
        listJoinedByMDN.forEach { line ->
            out.println(line[0] + "," + line[1] + "," + line[4] + "," + line[6])
        }
    }

    File("Result_3_Kresimir_Hajduk.csv").printWriter().use { out ->
        out.println("Resale Plan,Number of Devices")
        resalePlanMap.forEach { (key, value) ->
            out.println("$key,$value")
        }
    }
}
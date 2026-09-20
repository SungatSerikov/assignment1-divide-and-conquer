import csv
import os
import math
import matplotlib.pyplot as plt

os.makedirs("docs/plots", exist_ok=True)

data = {}

with open("results/results.csv", newline="") as file:
    for row in csv.DictReader(file):

        if row["inputType"] not in ("random", "random-points"):
            continue

        algorithm = row["algorithm"]

        if algorithm not in data:
            data[algorithm] = {
                "n": [],
                "time": [],
                "depth": []
            }

        data[algorithm]["n"].append(int(row["n"]))

        # ns -> ms
        data[algorithm]["time"].append(
            float(row["avgTimeNs"]) / 1_000_000
        )

        data[algorithm]["depth"].append(
            float(row["avgMaxDepth"])
        )


# =============================
# Runtime vs n
# =============================

for algorithm, values in data.items():
    plt.plot(
        values["n"],
        values["time"],
        marker="o",
        label=algorithm
    )


# Theoretical n log n for MergeSort
n_values = data["MergeSort"]["n"]
merge_time = data["MergeSort"]["time"]

theory_nlogn = [
    n * math.log2(n)
    for n in n_values
]

# Scale theory to measured MergeSort
scale = merge_time[0] / theory_nlogn[0]

theory_nlogn = [
    value * scale
    for value in theory_nlogn
]

plt.plot(
    n_values,
    theory_nlogn,
    linestyle="--",
    label="Theoretical n log n"
)


plt.xscale("log", base=2)
plt.yscale("log")

plt.xticks(
    [1000, 2000, 4000, 8000, 16000, 32000],
    ["1k", "2k", "4k", "8k", "16k", "32k"]
)

plt.xlabel("Input size n")
plt.ylabel("Average time (ms)")
plt.title("Runtime vs Input Size")

plt.legend()
plt.grid(True, which="both")
plt.tight_layout()

plt.savefig(
    "docs/plots/runtime-vs-n.png",
    dpi=300
)

plt.close()


# =============================
# Recursion depth vs n
# =============================

for algorithm, values in data.items():
    plt.plot(
        values["n"],
        values["depth"],
        marker="o",
        label=algorithm
    )


# Theoretical log2(n)
n_values = data["MergeSort"]["n"]
merge_depth = data["MergeSort"]["depth"]

theory_log = [
    math.log2(n)
    for n in n_values
]

# Shift theoretical line so it starts near measured depth
offset = merge_depth[0] - theory_log[0]

theory_log = [
    value + offset
    for value in theory_log
]

plt.plot(
    n_values,
    theory_log,
    linestyle="--",
    label="Theoretical log2(n)"
)


plt.xscale("log", base=2)

plt.xticks(
    [1000, 2000, 4000, 8000, 16000, 32000],
    ["1k", "2k", "4k", "8k", "16k", "32k"]
)

plt.xlabel("Input size n")
plt.ylabel("Average maximum recursion depth")
plt.title("Recursion Depth vs Input Size")

plt.legend()
plt.grid(True)
plt.tight_layout()

plt.savefig(
    "docs/plots/depth-vs-n.png",
    dpi=300
)

plt.close()


print("Plots created successfully.")
# =============================
# Operations vs n
# =============================

operations = {}

with open("results/results.csv", newline="") as file:
    for row in csv.DictReader(file):

        algorithm = row["algorithm"]
        input_type = row["inputType"]

        # Random input for the three array algorithms
        if (
                algorithm in (
                "MergeSort",
                "QuickSort",
                "DeterministicSelect"
        )
                and input_type == "random"
        ):
            if algorithm not in operations:
                operations[algorithm] = {
                    "n": [],
                    "ops": []
                }

            operations[algorithm]["n"].append(
                int(row["n"])
            )

            operations[algorithm]["ops"].append(
                float(row["avgOperations"])
            )


# Real measured comparisons
for algorithm, values in operations.items():
    plt.plot(
        values["n"],
        values["ops"],
        marker="o",
        label=algorithm
    )


# -----------------------------
# Theoretical n log n reference
# -----------------------------

n_values = operations["MergeSort"]["n"]

theory_nlogn = [
    n * math.log2(n)
    for n in n_values
]

merge_ops = operations["MergeSort"]["ops"]

scale_nlogn = (
        merge_ops[0] / theory_nlogn[0]
)

theory_nlogn = [
    value * scale_nlogn
    for value in theory_nlogn
]

plt.plot(
    n_values,
    theory_nlogn,
    linestyle="--",
    label="Scaled n log n reference"
)


# -----------------------------
# Theoretical n reference
# -----------------------------

select_ops = operations[
    "DeterministicSelect"
]["ops"]

theory_n = n_values.copy()

scale_n = (
        select_ops[0] / theory_n[0]
)

theory_n = [
    value * scale_n
    for value in theory_n
]

plt.plot(
    n_values,
    theory_n,
    linestyle="--",
    label="Scaled n reference"
)


plt.xscale("log", base=2)
plt.yscale("log")

plt.xticks(
    [1000, 2000, 4000, 8000, 16000, 32000],
    ["1k", "2k", "4k", "8k", "16k", "32k"]
)

plt.xlabel("Input size n")
plt.ylabel("Average element comparisons")
plt.title("Operations vs Input Size")

plt.legend()
plt.grid(True, which="both")
plt.tight_layout()

plt.savefig(
    "docs/plots/operations-vs-n.png",
    dpi=300
)

plt.close()

print("Operations plot created successfully.")